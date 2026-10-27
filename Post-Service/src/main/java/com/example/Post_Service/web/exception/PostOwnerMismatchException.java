package com.example.Post_Service.web.exception;

public class PostOwnerMismatchException extends RuntimeException {
    public PostOwnerMismatchException(String message) {
        super(message);
    }
}
