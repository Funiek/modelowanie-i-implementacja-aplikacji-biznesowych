package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Allow the controller to return a 404 if an account is not found by simply
 * throwing this exception. The @ResponseStatus causes Spring MVC to return a
 * 404 instead of the usual 500.
 * 
 * @author Paul Chapman
 */
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class UserNotAdminException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 2L;

	public UserNotAdminException() {
		super("User is not an admin");
	}
}
