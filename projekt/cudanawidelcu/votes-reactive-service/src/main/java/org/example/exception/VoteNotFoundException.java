package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;


/**
 * Exception thrown when a vote is not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class VoteNotFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new VoteNotFoundException with the specified vote name.
	 *
	 * @param voteName the name of the vote that was not found
	 */
	public VoteNotFoundException(String voteName) {
		super("No such vote: " + voteName);
	}
}
