package com.example.Post_Service.web.exception;

public class UserDoesntHavePermissionsException extends RuntimeException{
    public  UserDoesntHavePermissionsException(String message)  {
        super(message);
    }
}
