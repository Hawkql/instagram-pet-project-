package com.example.User_Service.web.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String param) {
        super(String.format("Пользователь не найден = %S",param));
    }
}
