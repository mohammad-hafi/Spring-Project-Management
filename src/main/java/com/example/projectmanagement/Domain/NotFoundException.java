package com.example.projectmanagement.Domain;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}
