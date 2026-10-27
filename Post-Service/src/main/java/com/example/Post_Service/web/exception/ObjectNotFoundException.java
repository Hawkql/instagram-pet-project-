package com.example.Post_Service.web.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String string) {
        super("Нет объекта в s3"+string);
    }
}
