package com.vishal.user_service.service.impl;

import com.vishal.user_service.exception.UserNotFoundException;
import com.vishal.user_service.mapper.UserMapper;
import com.vishal.user_service.model.Users;
import com.vishal.user_service.payload.request.PasswordUpdateRequest;
import com.vishal.user_service.payload.request.PasswordUpdateWithOtp;
import com.vishal.user_service.payload.request.RegisterRequest;
import com.vishal.user_service.repo.IUserRepo;
import com.vishal.user_service.service.IOtpService;
import com.vishal.user_service.service.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IOtpService otpService;

    @Override
    public Users createUser(RegisterRequest users) {
        users.setPassword(encoder.encode(users.getPassword()));
        return userRepo.save(UserMapper.toEntity(users));
    }

    @Override
    public List<Users> createBulkUsers(List<RegisterRequest> users) {
        List<Users> userEntities = users.stream()
                .map(user -> {
                    user.setPassword(encoder.encode(user.getPassword()));
                    return UserMapper.toEntity(user);
                })
                .collect(Collectors.toList());

        return userRepo.saveAll(userEntities);
    }

    @Override
    public List<Users> findAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public Users findUserById(UUID id) throws UserNotFoundException {
        Optional<Users> user = userRepo.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Users not found with this id - "+id);
        }
        return user.get();
    }

    @Override
    public Users updateUser(Users users, UUID userId) throws UserNotFoundException {
        Optional<Users> foundUser = userRepo.findById(userId);
        if (foundUser.isEmpty()) {
            throw new UserNotFoundException("Users not found with this id:- "+userId);
        }
        Users existingUsers = foundUser.get();
        existingUsers.setFullName(users.getFullName());
        existingUsers.setPhone(users.getPhone());
        existingUsers.setRole(users.getRole());

        return userRepo.save(existingUsers);
    }

    @Override
    public Users findUserByEmail(String email) throws UserNotFoundException {
        Optional<Users> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Users with this mail is not found: - "+email);
        }
        return user.get();
    }

    @Override
    public void updatePassword(UUID userId, PasswordUpdateRequest request) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);
    }

    @Override
    public void resetPasswordWithOtp(PasswordUpdateWithOtp request) {
        Users user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not registered"));

        if (!otpService.validateOtp(request.getEmail(), request.getOtp())) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);

        otpService.invalidateOtp(request.getEmail());
    }
}
