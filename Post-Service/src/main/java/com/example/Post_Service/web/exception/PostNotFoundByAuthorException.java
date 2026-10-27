package com.example.Post_Service.web.exception;

public class PostNotFoundByAuthorException extends RuntimeException {
    public PostNotFoundByAuthorException(String message) {
        super(message);
    }
}
