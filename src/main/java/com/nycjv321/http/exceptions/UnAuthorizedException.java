package com.nycjv321.http.exceptions;

/**
 * This exception is thrown when the request was not authorized
 */
public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException(String message) {
        super(message);
    }
}
