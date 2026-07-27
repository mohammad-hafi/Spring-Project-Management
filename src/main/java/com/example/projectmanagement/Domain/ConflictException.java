package com.example.projectmanagement.Domain;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
}
