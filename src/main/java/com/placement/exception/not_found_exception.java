package com.placement.exception;

/**
 * Thrown whenever a referenced entity (student, company, drive, application)
 * cannot be found. Mapped to HTTP 404 by global_exception_handler.
 */
public class not_found_exception extends RuntimeException {
    public not_found_exception(String message) {
        super(message);
    }
}
