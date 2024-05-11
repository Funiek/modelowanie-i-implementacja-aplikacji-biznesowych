package pl.cudanawidelcu.microservices.services.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.cudanawidelcu.microservices.services.web.dto.RecipeDto;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class WebRecipesService {
	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	protected String serviceUrl;

	protected Logger logger = Logger.getLogger(WebRecipesService.class
			.getName());

	public WebRecipesService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl
				: "http://" + serviceUrl;
	}
	@PostConstruct
	public void demoOnly() {
		logger.warning("The RestTemplate request factory is " + restTemplate.getRequestFactory().getClass());
	}

	public List<RecipeDto> getAll() {
		RecipeDto[] recipeDtos = null;

		try {
			recipeDtos = restTemplate.getForObject(serviceUrl + "/api/v1/recipes", RecipeDto[].class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), this.getClass().getEnclosingMethod().getName(), e);
		}

		return (recipeDtos == null || recipeDtos.length == 0) ? null : Arrays.asList(recipeDtos);
	}
}
