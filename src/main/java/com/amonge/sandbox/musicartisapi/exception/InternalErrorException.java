package com.amonge.sandbox.musicartisapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Customize exception to throw when something happens and the application can't handle it.
 * For example: when the service to get the artist information is down.
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalErrorException extends RuntimeException {

    //not used
    public InternalErrorException(String message) {
        super(message);
    }

    public InternalErrorException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
