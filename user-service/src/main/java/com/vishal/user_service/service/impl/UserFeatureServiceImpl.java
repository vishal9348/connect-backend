package com.vishal.user_service.service.impl;

import com.vishal.user_service.exception.*;
import com.vishal.user_service.model.Users;
import com.vishal.user_service.payload.request.UpdateProfileRequest;
import com.vishal.user_service.payload.response.UserResponse;
import com.vishal.user_service.repo.IUserRepo;
import com.vishal.user_service.security.JwtUtils;
import com.vishal.user_service.service.IEventPublisher;
import com.vishal.user_service.service.IUserFeatureService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserFeatureServiceImpl implements IUserFeatureService {

    private final IUserRepo userRepo;
    private final IEventPublisher eventPublisher;
    private final JwtUtils jwtUtils;
//  private final ICacheManager cacheManager;

    @Override
    public UserResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (request.fullName() != null) {
            user.setFullName(request.fullName());
        }
        if (request.bio() != null) {
            user.setBio(request.bio());
        }
        if (request.profileImageUrl() != null) {
            user.setProfileImageUrl(request.profileImageUrl());
        }

        Users updatedUser = userRepo.save(user);
//        cacheManager.evictUserCache(user.getUsername());
        eventPublisher.publishProfileUpdatedEvent(userId);

        return UserResponse.from(updatedUser);
    }

    @Override
    public void verifyToken(String token) {
        // Implementation would depend on your JWT service
        // Typically this would validate the token structure and signature
        jwtUtils.validateToken(token);
    }

    @Override
//    @Cacheable(value = "users", key = "#username")
    public UserResponse getByUsername(String username) {
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return UserResponse.from(user);
    }

    @Override
    @Transactional
    public void followUser(UUID followerId, String targetUsername) throws AlreadyFollowingException {
        Users follower = userRepo.findById(followerId)
                .orElseThrow(() -> new UserNotFoundException("Follower not found with ID - " + followerId));
        Users target = userRepo.findByUsername(targetUsername)
                .orElseThrow(() -> new UserNotFoundException(targetUsername));

        if (follower.getFollowingIds().contains(target.getId())) {
            throw new AlreadyFollowingException("You already following this user");
        }

        // Add target user to follower's following list
        follower.getFollowingIds().add(target.getId());
        follower.setTotalFollowing(follower.getFollowingIds().size());

        // Add follower to target user's followers list
        target.getFollowerIds().add(follower.getId());
        target.setTotalFollowers(target.getFollowerIds().size());

        // Save both users
        userRepo.save(follower);
        userRepo.save(target);

//        cacheManager.evictUserCache(target.getUsername());
//        eventPublisher.publishFollowEvent(followerId, target.getId());
    }

    @Override
    @Transactional
    public void unfollowUser(UUID followerId, String targetUsername) {
        Users follower = userRepo.findById(followerId)
                .orElseThrow(() -> new UserNotFoundException("Follower not found with ID - " + followerId));
        Users target = userRepo.findByUsername(targetUsername)
                .orElseThrow(() -> new UserNotFoundException(targetUsername));

        if (!follower.getFollowingIds().contains(target.getId())) {
            throw new NotFollowingException("You are not following this user");
        }

        // Remove target from follower's following list
        follower.getFollowingIds().remove(target.getId());
        follower.setTotalFollowing(follower.getFollowingIds().size());

        // Remove follower from target user's followers list
        target.getFollowerIds().remove(follower.getId());
        target.setTotalFollowers(target.getFollowerIds().size());

        // Save both users
        userRepo.save(follower);
        userRepo.save(target);

        // cacheManager.evictUserCache(target.getUsername());
        // eventPublisher.publishUnfollowEvent(followerId, target.getId());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateRoles(UUID userId, Set<String> roles) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with this ID - " + userId));

        validateRoles(roles);
        user.setRole(roles);
        Users savedUser = userRepo.save(user);

//        cacheManager.evictUserCache(user.getUsername());
//        eventPublisher.publishRolesUpdatedEvent(userId, roles);
        return UserResponse.from(savedUser);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deactivateUser(UUID userId) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (!user.isActive()) {
            throw new UserAlreadyDeactivatedException(userId.toString());
        }

        user.setActive(false);
        userRepo.save(user);

//        cacheManager.evictUserCache(user.getUsername());
//        eventPublisher.publishUserDeactivatedEvent(userId);
    }

    private void validateRoles(Set<String> roles) {
        Set<String> validRoles = Set.of("ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR");
        if (!validRoles.containsAll(roles)) {
            throw new InvalidRoleException("Invalid roles provided: " + roles);
        }
    }
}