package com.example.demo.domain.exception;

/**
 * @author Joel NOUMIA
 */

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

}
