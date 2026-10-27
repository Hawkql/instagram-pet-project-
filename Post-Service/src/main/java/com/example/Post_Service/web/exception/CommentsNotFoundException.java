package com.example.Post_Service.web.exception;

public class CommentsNotFoundException extends RuntimeException {
    public CommentsNotFoundException(String message) {
        super(message);
    }
}
