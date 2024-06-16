package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;


/**
 * Exception thrown when a user is not authorized as an admin.
 */
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class UserNotAdminException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 2L;

	/**
	 * Constructs a new UserNotAdminException with the default message.
	 */
	public UserNotAdminException() {
		super("User is not an admin");
	}
}
