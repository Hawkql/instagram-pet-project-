package com.example.Post_Service.web.exception;

public class PostAlreadyLikedException extends RuntimeException {
    public PostAlreadyLikedException(String message) {
        super(message);
    }
}
