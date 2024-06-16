package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Exception thrown when a product is not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for ProductNotFoundException.
	 *
	 * @param productName name of the product that was not found
	 */
	public ProductNotFoundException(String productName) {
		super("No such product: " + productName);
	}
}