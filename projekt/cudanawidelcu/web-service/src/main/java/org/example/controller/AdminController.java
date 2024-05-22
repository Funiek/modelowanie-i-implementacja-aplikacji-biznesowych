package org.example.controller;

import org.example.service.IdentityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller("/admin")
public class AdminController {
    private final IdentityService identityService;

    public AdminController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @GetMapping("/admin-panel")
    public String adminPanel() {
        return "adminPanel";
    }
    @GetMapping("/manage-users")
    public String manageUsers() {
        return "manageUsers";
    }
    @GetMapping("/manage-recipes")
    public String manageProducts() {
        return "manageRecipes";
    }
    @GetMapping("/edit-recipe/{id}")
    public String manageProducts(@PathVariable("id") Long id) {
        return "editRecipe";
    }
}
