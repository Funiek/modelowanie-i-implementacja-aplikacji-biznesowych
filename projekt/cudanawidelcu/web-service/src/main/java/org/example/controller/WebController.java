package org.example.controller;

import org.example.dto.RecipeDto;
import org.example.request.RateRecipeRequest;
import org.example.request.UpdateRatingRequest;
import org.example.response.UpdateRatingResponse;
import org.example.service.RecipesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class WebController {

    private final RecipesService recipesService;

    protected Logger logger = Logger.getLogger(WebController.class.getName());

    public WebController(RecipesService recipesService) {
        this.recipesService = recipesService;
    }


    @RequestMapping("/")
    public String index(Model model) {
        List<RecipeDto> recipeDtos = recipesService.getAll();
        model.addAttribute("recipeDtos", recipeDtos);
        return "index";
    }

    @RequestMapping("/category/{categoryName}")
    public String getByCategory(@PathVariable("categoryName") String categoryName, Model model) {
        List<RecipeDto> recipeDtos = recipesService.getByCategory(categoryName);
        model.addAttribute("recipeDtos", recipeDtos);
        return "index";
    }

    @RequestMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        RecipeDto recipeDto = recipesService.get(id);
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
        RecipeDto recipeDto = recipesService.get(updateRatingRequest.getId());

        UpdateRatingResponse updateRatingResponse = new UpdateRatingResponse();
        updateRatingResponse.setRating(recipeDto.getRating());
        updateRatingResponse.setCountVotes(recipeDto.getCountVotes());

        return updateRatingResponse;
    }
}
