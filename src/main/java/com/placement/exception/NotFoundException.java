package com.placement.exception;

/**
 * Thrown whenever a referenced entity (student, company, drive, application)
 * cannot be found. Mapped to HTTP 404 by GlobalExceptionHandler.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
