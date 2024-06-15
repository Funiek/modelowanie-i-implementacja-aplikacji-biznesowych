package org.example.controller;

import org.example.dto.CategoryDto;
import org.example.dto.RecipeDto;
import org.example.request.*;
import org.example.response.RecipesFindByIdResponse;
import org.example.response.VotesRatingByRecipeIdResponse;
import org.example.response.VotesSaveResponse;
import org.example.service.ImageService;
import org.example.service.ProductsService;
import org.example.service.RecipesService;
import org.example.service.VotesService;
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


    @RequestMapping("/")
    public String index(Model model) {
        List<RecipeDto> recipeDtos =  recipesService.findAll().stream()
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

    @RequestMapping("/category/{category}")
    public String findAllByCategory(@PathVariable("category") CategoryDto categoryDto, Model model) {
        List<RecipeDto> recipeDtos =  recipesService.findAllByCategory(categoryDto).stream()
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

    @RequestMapping(value = "/votes/rating", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public VotesRatingByRecipeIdResponse rate(@RequestBody VotesSaveRequest votesSaveRequest) {
        VotesSaveResponse votesSaveResponse = votesService.save(votesSaveRequest);
        return votesService.ratingByRecipeId(votesSaveResponse.getRecipeId());
    }

    @GetMapping("/recipes/create")
    public String createRecipe(Model model) {
        model.addAttribute("recipeDto", new RecipeDto());
        return "createRecipe";
    }

    @PostMapping("/recipes/create")
    public String createRecipe(@ModelAttribute RecipeDto recipeDto,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               @CookieValue("jwtToken") String jwtToken) throws IOException {
        if (!imageFile.isEmpty()) {
            imageService.sendImage(imageFile, recipeDto.getName());
        }
        RecipeDto newRecipeDto = recipesService.save(recipeDto, jwtToken);
        if(!recipeDto.getProducts().isEmpty()) {
            productsService.saveAll(recipeDto.getProducts(), jwtToken, newRecipeDto.getId());
        }

        return "redirect:/";
    }

    @GetMapping("/img/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable("imageName") String imageName) {
        logger.info("ZDJECIE DO POBRANIA: " + imageName);
        return imageService.getImage(imageName);
    }
}
