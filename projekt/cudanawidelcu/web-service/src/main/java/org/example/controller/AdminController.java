package org.example.controller;

import org.example.response.ValidateAdminResponse;
import org.example.service.IdentityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String manageUsers(@CookieValue("jwtToken") String jwtToken) {
        ValidateAdminResponse validateAdminResponse = identityService.validateAdmin(jwtToken);
        if (validateAdminResponse.getIsValid()) {
            return "manageUsers";
        }

        return "redirect:/";
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
