package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Exception thrown when a user is not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6L;

    /**
     * Constructs a new UserNotFoundException with the default message.
     */
    public UserNotFoundException() {
        super("User not found");
    }

    /**
     * Constructs a new UserNotFoundException with a custom message.
     *
     * @param name the name of the user that was not found
     */
    public UserNotFoundException(String name) {
        super("User not found with name: " + name);
    }
}