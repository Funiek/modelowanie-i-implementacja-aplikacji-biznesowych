package org.example.controller;

import org.example.dto.CategoryDto;
import org.example.dto.RecipeDto;
import org.example.request.*;
import org.example.response.RecipesFindByIdResponse;
import org.example.response.VotesRatingByRecipeIdResponse;
import org.example.response.VotesSaveResponse;
import org.example.service.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller class for handling web endpoints related to recipes, votes, images, and categories.
 */
@Controller
public class WebController {

    private final RecipesService recipesService;
    private final ImageService imageService;
    private final VotesService votesService;
    private final ProductsService productsService;
    protected Logger logger = Logger.getLogger(WebController.class.getName());

    public WebController(RecipesService recipesService, ImageService imageService, VotesService votesService, ProductsService productsService) {
        this.recipesService = recipesService;
        this.imageService = imageService;
        this.votesService = votesService;
        this.productsService = productsService;
    }

    /**
     * Endpoint for the home page displaying all recipes.
     *
     * @param model Spring MVC model
     * @return view name for the home page
     */
    @RequestMapping("/")
    public String index(Model model) {
        List<RecipeDto> recipeDtos = recipesService.findAll().stream()
                .map(recipe -> {
                    VotesRatingByRecipeIdResponse rating = votesService.ratingByRecipeId(recipe.getId());
                    return RecipeDto.builder()
                            .id(recipe.getId())
                            .name(recipe.getName())
                            .category(recipe.getCategory())
                            .votes(recipe.getVotes())
                            .products(recipe.getProducts())
                            .description(recipe.getDescription())
                            .countVotes(rating.getCountVotes())
                            .rating(rating.getRating())
                            .build();
                }).collect(Collectors.toList());

        model.addAttribute("recipeDtos", recipeDtos);
        return "index";
    }

    /**
     * Endpoint for displaying recipes by category.
     *
     * @param categoryDto category DTO object
     * @param model       Spring MVC model
     * @return view name for the recipes page filtered by category
     */
    @RequestMapping("/category/{category}")
    public String findAllByCategory(@PathVariable("category") CategoryDto categoryDto, Model model) {
        List<RecipeDto> recipeDtos = recipesService.findAllByCategory(categoryDto).stream()
                .map(recipe -> {
                    VotesRatingByRecipeIdResponse rating = votesService.ratingByRecipeId(recipe.getId());
                    return RecipeDto.builder()
                            .id(recipe.getId())
                            .name(recipe.getName())
                            .category(recipe.getCategory())
                            .votes(recipe.getVotes())
                            .products(recipe.getProducts())
                            .description(recipe.getDescription())
                            .countVotes(rating.getCountVotes())
                            .rating(rating.getRating())
                            .build();
                }).collect(Collectors.toList());

        model.addAttribute("recipeDtos", recipeDtos);
        return "index";
    }

    /**
     * Endpoint for displaying recipe details.
     *
     * @param id    recipe ID
     * @param model Spring MVC model
     * @return view name for the recipe details page
     */
    @RequestMapping("/recipes/details/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        RecipesFindByIdResponse recipe = recipesService.findById(id);
        VotesRatingByRecipeIdResponse rating = votesService.ratingByRecipeId(recipe.getId());
        RecipeDto recipeDto = RecipeDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .category(recipe.getCategory())
                .votes(recipe.getVotes())
                .products(recipe.getProducts())
                .description(recipe.getDescription())
                .countVotes(rating.getCountVotes())
                .rating(rating.getRating())
                .build();

        model.addAttribute("recipeDto", recipeDto);
        return "details";
    }

    /**
     * Endpoint for saving user rating on a recipe.
     *
     * @param votesSaveRequest request object containing the rating and recipe ID
     * @return response object containing the updated rating and vote count
     */
    @RequestMapping(value = "/votes/rating", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public VotesRatingByRecipeIdResponse rate(@RequestBody VotesSaveRequest votesSaveRequest) {
        VotesSaveResponse votesSaveResponse = votesService.save(votesSaveRequest);
        return votesService.ratingByRecipeId(votesSaveResponse.getRecipeId());
    }

    /**
     * Endpoint for displaying the form to create a new recipe.
     *
     * @param model Spring MVC model
     * @return view name for the create recipe form
     */
    @GetMapping("/recipes/create")
    public String createRecipe(Model model) {
        model.addAttribute("recipeDto", new RecipeDto());
        return "createRecipe";
    }

    /**
     * Endpoint for processing the form submission to create a new recipe.
     *
     * @param recipeDto  recipe DTO object containing the recipe details
     * @param imageFile  image file to be uploaded for the recipe
     * @param jwtToken   JWT token for authentication
     * @return redirects to the home page after creating the recipe
     * @throws IOException if there is an error handling the image file
     */
    @PostMapping("/recipes/create")
    public String createRecipe(@ModelAttribute RecipeDto recipeDto,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               @CookieValue("jwtToken") String jwtToken) throws IOException {
        if (!imageFile.isEmpty()) {
            imageService.sendImage(imageFile, recipeDto.getName());
        }
        RecipeDto newRecipeDto = recipesService.save(recipeDto, jwtToken);
        if (!recipeDto.getProducts().isEmpty()) {
            productsService.saveAll(recipeDto.getProducts(), jwtToken, newRecipeDto.getId());
        }

        return "redirect:/";
    }

    /**
     * Endpoint for downloading images associated with recipes.
     *
     * @param imageName name of the image file
     * @return ResponseEntity with the image resource
     */
    @GetMapping("/img/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable("imageName") String imageName) {
        logger.info("Image to download: " + imageName);
        return imageService.getImage(imageName);
    }
}