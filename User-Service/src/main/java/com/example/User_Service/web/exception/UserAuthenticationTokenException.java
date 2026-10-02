package com.example.User_Service.web.exception;

public class UserAuthenticationTokenException extends RuntimeException {
    public UserAuthenticationTokenException(){
        super("Токен не найден или не принадлежит вам ");

    }
}
