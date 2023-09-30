package com.project.ecommerce.exception;

public class CustomFileNotFoundException extends RuntimeException {

    public CustomFileNotFoundException(String message) {
        super(message);
    }
}