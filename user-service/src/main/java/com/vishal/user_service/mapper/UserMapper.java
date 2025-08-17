package com.vishal.user_service.mapper;

import com.vishal.user_service.model.Users;
import com.vishal.user_service.payload.request.RegisterRequest;

public class UserMapper {

    public static Users toEntity(RegisterRequest dto) {
        Users user = new Users();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFullName(dto.getFullName());
        return user;
    }
}
