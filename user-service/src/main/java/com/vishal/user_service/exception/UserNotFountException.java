package com.vishal.user_service.exception;

public class UserNotFountException extends RuntimeException{

    public UserNotFountException(String msg){
        super(msg);
    }
}
