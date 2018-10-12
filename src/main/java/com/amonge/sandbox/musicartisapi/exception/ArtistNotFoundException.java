package com.amonge.sandbox.musicartisapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Customize exception to throw when an artist's information is not found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ArtistNotFoundException extends RuntimeException {

    public ArtistNotFoundException(String message) {
        super(message);
    }

    public ArtistNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
