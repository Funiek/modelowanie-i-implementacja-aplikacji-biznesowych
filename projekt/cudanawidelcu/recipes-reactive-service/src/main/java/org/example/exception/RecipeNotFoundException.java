package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;


/**
 * Exception thrown when a recipe is not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecipeNotFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new RecipeNotFoundException with the given recipe name.
	 *
	 * @param recipeName the name of the recipe that was not found
	 */
	public RecipeNotFoundException(String recipeName) {
		super("No such recipe: " + recipeName);
	}
}