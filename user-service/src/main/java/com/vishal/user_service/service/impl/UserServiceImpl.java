package com.vishal.user_service.service.impl;

import com.vishal.user_service.exception.UserNotFountException;
import com.vishal.user_service.model.Users;
import com.vishal.user_service.repo.IUserRepo;
import com.vishal.user_service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Users createUser(Users users) {
        users.setPassword(encoder.encode(users.getPassword()));
        return userRepo.save(users);
    }

    @Override
    public List<Users> findAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public Users findUserById(UUID id) throws UserNotFountException {
        Optional<Users> user = userRepo.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFountException("Users not found with this id - "+id);
        }
        return user.get();
    }

    @Override
    public Users updateUser(Users users, UUID userId) throws UserNotFountException {
        Optional<Users> foundUser = userRepo.findById(userId);
        if (foundUser.isEmpty()) {
            throw new UserNotFountException("Users not found with this id:- "+userId);
        }
        Users existingUsers = foundUser.get();
        existingUsers.setFullName(users.getFullName());
        existingUsers.setPhone(users.getPhone());
        existingUsers.setRole(users.getRole());

        return userRepo.save(existingUsers);
    }

    @Override
    public Users findUserByEmail(String email) throws UserNotFountException {
        Optional<Users> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFountException("Users with this mail is not found: - "+email);
        }
        return user.get();
    }
}
