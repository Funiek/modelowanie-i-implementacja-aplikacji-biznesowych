package pl.cudanawidelcu.microservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Allow the controller to return a 404 if an account is not found by simply
 * throwing this exception. The @ResponseStatus causes Spring MVC to return a
 * 404 instead of the usual 500.
 * 
 * @author Paul Chapman
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecipeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RecipeNotFoundException(String recipeName) {
		super("No such recipe: " + recipeName);
	}
}
