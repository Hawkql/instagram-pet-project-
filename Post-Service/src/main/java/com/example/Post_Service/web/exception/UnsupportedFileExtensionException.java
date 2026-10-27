package com.example.Post_Service.web.exception;

public class UnsupportedFileExtensionException extends  RuntimeException{
    public UnsupportedFileExtensionException(String mes) {
        super("не поддерживается расширение "+ mes);
    }
}
