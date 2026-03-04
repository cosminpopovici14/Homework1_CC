package org.example.error;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) { super(msg); }
}
