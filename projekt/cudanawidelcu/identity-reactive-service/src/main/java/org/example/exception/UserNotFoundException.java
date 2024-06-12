package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    @Serial private static final long serialVersionUID = 6L;

    public UserNotFoundException() {
        super("User not found");
    }
    public UserNotFoundException(String name) {
        super("User not found with name: " + name);
    }
}
