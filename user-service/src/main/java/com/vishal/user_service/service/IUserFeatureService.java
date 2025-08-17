package com.vishal.user_service.service;

import com.vishal.user_service.exception.AlreadyFollowingException;
import com.vishal.user_service.payload.request.LoginRequest;
import com.vishal.user_service.payload.request.UpdateProfileRequest;
import com.vishal.user_service.payload.response.AuthResponse;
import com.vishal.user_service.payload.response.UserResponse;

import java.util.Set;
import java.util.UUID;

public interface IUserFeatureService {
    UserResponse updateProfile(UUID userId, UpdateProfileRequest request);
    void verifyToken(String token);
//    AuthResponse login(LoginRequest request);
    UserResponse getByUsername(String username);
    void followUser(UUID followerId, String targetUsername) throws AlreadyFollowingException;
    void unfollowUser(UUID followerId, String targetUsername);
    UserResponse updateRoles(UUID userId, Set<String> roles);
    void deactivateUser(UUID userId);
}
