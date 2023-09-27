package com.project.springbootangularcrud.exception;

public class CustomFileNotFoundException extends RuntimeException {

    public CustomFileNotFoundException(String message) {
        super(message);
    }
}