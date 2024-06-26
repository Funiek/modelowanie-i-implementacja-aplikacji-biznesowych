package org.example.controller;

import org.example.dto.RecipeDto;
import org.example.request.IdentityUserRoleRequest;
import org.example.request.IdentityUserUpdateRequest;
import org.example.request.ImagesRenameRequest;
import org.example.request.RecipesUpdateRequest;
import org.example.response.*;
import org.example.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller class for handling administrative operations.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final IdentityService identityService;
    private final RecipesService recipesService;
    private final ImageService imageService;
    private final VotesService votesService;

    public AdminController(IdentityService identityService, RecipesService recipesService,
                           ImageService imageService, VotesService votesService) {
        this.identityService = identityService;
        this.recipesService = recipesService;
        this.imageService = imageService;
        this.votesService = votesService;
    }

    /**
     * Endpoint for accessing the admin panel.
     *
     * @param jwtToken JWT token from the cookie
     * @return admin panel view if the user is validated as admin, otherwise redirects to the home page
     */
    @GetMapping("/panel")
    public String adminPanel(@CookieValue("jwtToken") String jwtToken) {
        IdentityValidateAdminResponse identityValidateAdminResponse = identityService.validateAdmin(jwtToken);
        if (identityValidateAdminResponse.getIsValid()) {
            return "adminPanel";
        }

        return "redirect:/";
    }

    /**
     * Endpoint for managing users.
     *
     * @param jwtToken JWT token from the cookie
     * @param model    Spring MVC model
     * @return manage users view if the user is validated as admin, otherwise redirects to the home page
     */
    @GetMapping("/users/manage")
    public String manageUsers(@CookieValue("jwtToken") String jwtToken, Model model) {
        IdentityValidateAdminResponse identityValidateAdminResponse = identityService.validateAdmin(jwtToken);
        if (identityValidateAdminResponse.getIsValid()) {
            List<IdentityUserResponse> identityUserResponse = identityService.getAll(jwtToken);
            model.addAttribute("identityUserResponse", identityUserResponse);
            return "manageUsers";
        }

        return "redirect:/admin/users/manage";
    }

    /**
     * Endpoint for editing a user.
     *
     * @param id       ID of the user to edit
     * @param jwtToken JWT token from the cookie
     * @param model    Spring MVC model
     * @return edit user view if the user is validated as admin, otherwise redirects to the home page
     */
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, @CookieValue("jwtToken") String jwtToken, Model model) {
        IdentityValidateAdminResponse identityValidateAdminResponse = identityService.validateAdmin(jwtToken);
        if (identityValidateAdminResponse.getIsValid()) {
            IdentityUserResponse identityUserResponse = identityService.findById(jwtToken, id);
            model.addAttribute("userDto", IdentityUserUpdateRequest.builder()
                    .id(identityUserResponse.getId())
                    .username(identityUserResponse.getUsername())
                    .roleRequest(IdentityUserRoleRequest.valueOf(identityUserResponse.getRoleResponse().name()))
                    .build());
            return "editUser";
        }

        return "redirect:/";
    }

    /**
     * Endpoint for processing user update form submission.
     *
     * @param jwtToken        JWT token from the cookie
     * @param userUpdateRequest updated user data
     * @return redirects to manage users page if the user is validated as admin, otherwise redirects to the home page
     */
    @PostMapping(path = "/users/edit", consumes = "application/x-www-form-urlencoded")
    public String editUser(@CookieValue("jwtToken") String jwtToken, @ModelAttribute IdentityUserUpdateRequest userUpdateRequest) {
        IdentityValidateAdminResponse identityValidateAdminResponse = identityService.validateAdmin(jwtToken);
        if (identityValidateAdminResponse.getIsValid()) {
            identityService.update(userUpdateRequest.getId(), jwtToken, userUpdateRequest);
            return "redirect:/admin/users/manage";
        }

        return "redirect:/";
    }

    /**
     * Endpoint for deleting a user.
     *
     * @param id       ID of the user to delete
     * @param jwtToken JWT token from the cookie
     * @return redirects to manage users page
     */
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, @CookieValue("jwtToken") String jwtToken) {
        identityService.delete(id, jwtToken);
        return "redirect:/admin/users/manage";
    }

    /**
     * Endpoint for managing recipes.
     *
     * @param jwtToken JWT token from the cookie
     * @param model    Spring MVC model
     * @return manage recipes view if the user is validated as admin, otherwise redirects to the home page
     */
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

    /**
     * Endpoint for editing a recipe.
     *
     * @param id       ID of the recipe to edit
     * @param jwtToken JWT token from the cookie
     * @param model    Spring MVC model
     * @return edit recipe view if the user is validated as admin, otherwise redirects to the home page
     */
    @GetMapping("/recipes/edit/{id}")
    public String editRecipe(@PathVariable("id") Long id, @CookieValue("jwtToken") String jwtToken, Model model) {
        IdentityValidateAdminResponse identityValidateAdminResponse = identityService.validateAdmin(jwtToken);
        if (identityValidateAdminResponse.getIsValid()) {
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
            return "editRecipe";
        }

        return "redirect:/";
    }

    /**
     * Endpoint for processing recipe update form submission.
     *
     * @param jwtToken           JWT token from the cookie
     * @param recipesUpdateRequest updated recipe data
     * @return redirects to manage recipes page if the user is validated as admin, otherwise redirects to the home page
     */
    @PostMapping(path = "/recipes/edit", consumes = "application/x-www-form-urlencoded")
    public String editRecipe(@CookieValue("jwtToken") String jwtToken, @ModelAttribute RecipesUpdateRequest recipesUpdateRequest) {
        IdentityValidateAdminResponse identityValidateAdminResponse = identityService.validateAdmin(jwtToken);
        if (identityValidateAdminResponse.getIsValid()) {
            RecipesFindByIdResponse oldRecipe = recipesService.findById(recipesUpdateRequest.getId());
            recipesService.update(recipesUpdateRequest.getId(), jwtToken, recipesUpdateRequest);

            if (!Objects.equals(oldRecipe.getName(), recipesUpdateRequest.getName())) {
                ImagesRenameRequest imagesRenameRequest = ImagesRenameRequest.builder()
                        .oldName(oldRecipe.getName() + ".jpeg")
                        .newName(recipesUpdateRequest.getName() + ".jpeg")
                        .build();
                imageService.renameImage(jwtToken, imagesRenameRequest);
            }

            return "redirect:/admin/recipes/manage";
        }

        return "redirect:/";
    }

    /**
     * Endpoint for deleting a recipe.
     *
     * @param id       ID of the recipe to delete
     * @param jwtToken JWT token from the cookie
     * @return redirects to manage recipes page
     */
    @GetMapping("/recipes/delete/{id}")
    public String deleteRecipe(@PathVariable("id") Long id, @CookieValue("jwtToken") String jwtToken) {
        IdentityValidateAdminResponse identityValidateAdminResponse = identityService.validateAdmin(jwtToken);
        if (identityValidateAdminResponse.getIsValid()) {
            recipesService.delete(id, jwtToken);
        }

        return "redirect:/admin/recipes/manage";
    }
}