package pl.cudanawidelcu.microservices.services.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.cudanawidelcu.microservices.services.web.Request.RateRecipeRequest;
import pl.cudanawidelcu.microservices.services.web.Response.UpdateRatingResponse;
import pl.cudanawidelcu.microservices.services.web.dto.RecipeDto;
import pl.cudanawidelcu.microservices.services.web.Request.UpdateRatingRequest;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class WebRecipesController {
    @Autowired
    protected WebRecipesService recipesService;

    protected Logger logger = Logger.getLogger(WebRecipesController.class.getName());

    public WebRecipesController(WebRecipesService recipesService) {
        this.recipesService = recipesService;
    }

    @RequestMapping("/")
    public String index(Model model) {
        List<RecipeDto> recipeDtos = recipesService.getAll();
        model.addAttribute("recipeDtos", recipeDtos);
        return "index";
    }

    @RequestMapping("/details/{recipeName}")
    public String details(@PathVariable("recipeName") String recipeName, Model model) {
        RecipeDto recipeDto = recipesService.getByName(recipeName);
        model.addAttribute("recipeDto", recipeDto);
        return "details";
    }

    @RequestMapping(value = "/rate", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public RecipeDto rate(@RequestBody RateRecipeRequest rateRecipeRequest) {
        return recipesService.rate(rateRecipeRequest);
    }

    @RequestMapping(value = "/updaterating", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public UpdateRatingResponse rate(@RequestBody UpdateRatingRequest updateRatingRequest) {
        RecipeDto recipeDto = recipesService.getByName(updateRatingRequest.getName());

        UpdateRatingResponse updateRatingResponse = new UpdateRatingResponse();
        updateRatingResponse.setRating(recipeDto.getRating());
        updateRatingResponse.setCountVotes(recipeDto.getCountVotes());

        return updateRatingResponse;
    }
}
