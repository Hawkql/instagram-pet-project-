package com.example.User_Service.web.exception;

public class UnsupportedFileExtensionException extends  RuntimeException{
    public  UnsupportedFileExtensionException(String param){
        super("файл не поддерживается "+param);
    }
}
