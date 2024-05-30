package org.example.controller;

import org.example.dto.RecipeDto;
import org.example.request.ChangeFileNameRequest;
import org.example.response.UserResponse;
import org.example.response.ValidateAdminResponse;
import org.example.service.IdentityService;
import org.example.service.ImageService;
import org.example.service.RecipesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final IdentityService identityService;

    private final RecipesService recipesService;
    private final ImageService imageService;

    public AdminController(IdentityService identityService, RecipesService recipesService, ImageService imageService) {
        this.identityService = identityService;
        this.recipesService = recipesService;
        this.imageService = imageService;
    }

    @GetMapping("/panel")
    public String adminPanel(@CookieValue("jwtToken") String jwtToken) {
        ValidateAdminResponse validateAdminResponse = identityService.validateAdmin(jwtToken);
        if (validateAdminResponse.getIsValid()) {
            return "adminPanel";
        }

        return "redirect:/";
    }
    @GetMapping("/users/manage")
    public String manageUsers(@CookieValue("jwtToken") String jwtToken, Model model) {
        ValidateAdminResponse validateAdminResponse = identityService.validateAdmin(jwtToken);
        if (validateAdminResponse.getIsValid()) {
            List<UserResponse> userResponses = identityService.getAll(jwtToken);
            model.addAttribute("userResponses", userResponses);
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
        ValidateAdminResponse validateAdminResponse = identityService.validateAdmin(jwtToken);
        if (validateAdminResponse.getIsValid()) {
            List<RecipeDto> recipeDtos = recipesService.getAll();
            model.addAttribute("recipeDtos", recipeDtos);
            return "manageRecipes";
        }

        return "redirect:/";
    }
    @GetMapping("/recipes/edit/{id}")
    public String updateProducts(@PathVariable("id") Long id, @CookieValue("jwtToken") String jwtToken, Model model) {
        ValidateAdminResponse validateAdminResponse = identityService.validateAdmin(jwtToken);
        if (validateAdminResponse.getIsValid()) {
            RecipeDto recipeDto = recipesService.get(id);
            model.addAttribute("recipeDto", recipeDto);
            return "editRecipe";
        }

        return "redirect:/";
    }

    @PostMapping(path = "/recipes/edit", consumes = "application/x-www-form-urlencoded")
    public String updateProducts(@CookieValue("jwtToken") String jwtToken, @ModelAttribute RecipeDto recipeDto) {
        ValidateAdminResponse validateAdminResponse = identityService.validateAdmin(jwtToken);
        if (validateAdminResponse.getIsValid()) {
            RecipeDto oldRecipe = recipesService.get(recipeDto.getId());
            recipesService.update(recipeDto.getId(), jwtToken, recipeDto);
            ChangeFileNameRequest changeFileNameRequest = ChangeFileNameRequest.builder()
                    .oldName(oldRecipe.getName() + ".jpeg")
                    .newName(recipeDto.getName() + ".jpeg")
                    .build();
            imageService.renameImage(jwtToken, changeFileNameRequest);
            return "redirect:/admin/recipes/manage";
        }

        return "redirect:/";
    }

    @GetMapping("/recipes/delete/{id}")
    public String deleteRecipe(@PathVariable("id") Long id, @CookieValue("jwtToken") String jwtToken) {
        ValidateAdminResponse validateAdminResponse = identityService.validateAdmin(jwtToken);
        if (validateAdminResponse.getIsValid()) {
            recipesService.delete(id, jwtToken);
        }

        return "redirect:/admin/recipes/manage";
    }
}
