package com.vishal.user_service.exception;

public class UserAlreadyDeactivatedException extends RuntimeException {
    public UserAlreadyDeactivatedException(String userId) {
        super("User already deactivated: " + userId);
    }
}
