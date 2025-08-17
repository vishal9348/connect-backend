package com.vishal.user_service.service;

import com.vishal.user_service.exception.UserNotFoundException;
import com.vishal.user_service.model.Users;
import com.vishal.user_service.payload.request.*;
import com.vishal.user_service.payload.response.AuthResponse;
import com.vishal.user_service.payload.response.UserResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IUserService {

    Users createUser(RegisterRequest users);
    List<Users> createBulkUsers(List<RegisterRequest> users);
    List<Users> findAllUsers();
    Users findUserById(UUID id) throws UserNotFoundException;
    Users updateUser(Users users, UUID userId) throws UserNotFoundException;
    Users findUserByEmail(String email) throws UserNotFoundException;
    void updatePassword(UUID userId, PasswordUpdateRequest request);
    void resetPasswordWithOtp(PasswordUpdateWithOtp request);
}
