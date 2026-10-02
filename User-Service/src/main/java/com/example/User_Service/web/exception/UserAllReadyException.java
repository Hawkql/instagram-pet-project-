package com.example.User_Service.web.exception;

public class UserAllReadyException extends RuntimeException{
    public  UserAllReadyException(String param) {
        super(String.format("Пользователь уже существует с таким параметром = %s", param));
    }
}
