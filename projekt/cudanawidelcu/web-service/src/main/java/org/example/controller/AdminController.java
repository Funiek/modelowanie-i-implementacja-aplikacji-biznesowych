package org.example.controller;

import org.example.dto.RecipeDto;
import org.example.response.UserResponse;
import org.example.response.ValidateAdminResponse;
import org.example.service.IdentityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final IdentityService identityService;

    public AdminController(IdentityService identityService) {
        this.identityService = identityService;
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

        return "redirect:/";
    }

    @GetMapping("/users/delete/{id}")
    public void deleteUser(@PathVariable("id") Long id, @CookieValue("jwtToken") String jwtToken) {
        identityService.delete(id, jwtToken);
    }
    @GetMapping("/recipes/manage")
    public String manageProducts(@CookieValue("jwtToken") String jwtToken) {
        ValidateAdminResponse validateAdminResponse = identityService.validateAdmin(jwtToken);
        if (validateAdminResponse.getIsValid()) {
            return "manageRecipes";
        }

        return "redirect:/";
    }
    @GetMapping("/recipes/edit/{id}")
    public String manageProducts(@PathVariable("id") Long id, @CookieValue("jwtToken") String jwtToken) {
        ValidateAdminResponse validateAdminResponse = identityService.validateAdmin(jwtToken);
        if (validateAdminResponse.getIsValid()) {
            return "editRecipe";
        }

        return "redirect:/";
    }
}
