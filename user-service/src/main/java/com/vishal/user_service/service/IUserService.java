package com.vishal.user_service.service;

import com.vishal.user_service.exception.UserNotFountException;
import com.vishal.user_service.model.Users;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    Users createUser(Users users);
    List<Users> findAllUsers();
    Users findUserById(UUID id) throws UserNotFountException;
    Users updateUser(Users users, UUID userId) throws UserNotFountException;
    Users findUserByEmail(String email) throws UserNotFountException;
}
