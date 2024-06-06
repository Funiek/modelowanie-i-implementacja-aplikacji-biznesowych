package org.example.controller;

import org.example.dto.RecipeDto;
import org.example.request.ImagesRenameRequest;
import org.example.request.RecipesUpdateRequest;
import org.example.response.*;
import org.example.service.IdentityService;
import org.example.service.ImageService;
import org.example.service.RecipesService;
import org.example.service.VotesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final IdentityService identityService;

    private final RecipesService recipesService;
    private final ImageService imageService;
    private final VotesService votesService;

    public AdminController(IdentityService identityService, RecipesService recipesService, ImageService imageService, VotesService votesService) {
        this.identityService = identityService;
        this.recipesService = recipesService;
        this.imageService = imageService;
        this.votesService = votesService;
    }

    @GetMapping("/panel")
    public String adminPanel(@CookieValue("jwtToken") String jwtToken) {
        IdentityValidateAdminResponse identityValidateAdminResponse = identityService.validateAdmin(jwtToken);
        if (identityValidateAdminResponse.getIsValid()) {
            return "adminPanel";
        }

        return "redirect:/";
    }
    @GetMapping("/users/manage")
    public String manageUsers(@CookieValue("jwtToken") String jwtToken, Model model) {
        IdentityValidateAdminResponse identityValidateAdminResponse = identityService.validateAdmin(jwtToken);
        if (identityValidateAdminResponse.getIsValid()) {
            List<IdentityUserResponse> identityUserRespons = identityService.getAll(jwtToken);
            model.addAttribute("userResponses", identityUserRespons);
            return "manageUsers";
        }

        return "redirect:/admin/users/manage";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, @CookieValue("jwtToken") String jwtToken) {
        identityService.delete(id, jwtToken);

        return "redirect:/";
    }
    @GetMapping("/recipes/manage")
    public String manageProducts(@CookieValue("jwtToken") String jwtToken, Model model) {
        IdentityValidateAdminResponse identityValidateAdminResponse = identityService.validateAdmin(jwtToken);
        if (identityValidateAdminResponse.getIsValid()) {
            List<RecipesFindAllResponse> recipesFindAllResponseList = recipesService.findAll();
            List<RecipeDto> recipeDtos = recipesFindAllResponseList.stream()
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
            return "manageRecipes";
        }

        return "redirect:/";
    }
    @GetMapping("/recipes/edit/{id}")
    public String updateProducts(@PathVariable("id") Long id, @CookieValue("jwtToken") String jwtToken, Model model) {
        IdentityValidateAdminResponse identityValidateAdminResponse = identityService.validateAdmin(jwtToken);
        if (identityValidateAdminResponse.getIsValid()) {
            RecipesFindByIdResponse recipe = recipesService.findById(id);
            model.addAttribute("recipe", recipe);
            return "editRecipe";
        }

        return "redirect:/";
    }

    @PostMapping(path = "/recipes/edit", consumes = "application/x-www-form-urlencoded")
    public String updateProducts(@CookieValue("jwtToken") String jwtToken, @ModelAttribute RecipesUpdateRequest recipesUpdateRequest) {
        IdentityValidateAdminResponse identityValidateAdminResponse = identityService.validateAdmin(jwtToken);
        if (identityValidateAdminResponse.getIsValid()) {
            RecipesFindByIdResponse oldRecipe = recipesService.findById(recipesUpdateRequest.getId());
            recipesService.update(recipesUpdateRequest.getId(), jwtToken, recipesUpdateRequest);
            ImagesRenameRequest imagesRenameRequest = ImagesRenameRequest.builder()
                    .oldName(oldRecipe.getName() + ".jpeg")
                    .newName(recipesUpdateRequest.getName() + ".jpeg")
                    .build();
            imageService.renameImage(jwtToken, imagesRenameRequest);
            return "redirect:/admin/recipes/manage";
        }

        return "redirect:/";
    }

    @GetMapping("/recipes/delete/{id}")
    public String deleteRecipe(@PathVariable("id") Long id, @CookieValue("jwtToken") String jwtToken) {
        IdentityValidateAdminResponse identityValidateAdminResponse = identityService.validateAdmin(jwtToken);
        if (identityValidateAdminResponse.getIsValid()) {
            recipesService.delete(id, jwtToken);
        }

        return "redirect:/admin/recipes/manage";
    }
}
