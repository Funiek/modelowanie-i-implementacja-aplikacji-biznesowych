package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.request.AuthenticationRequest;
import org.example.request.RegisterRequest;
import org.example.response.AuthenticationResponse;
import org.example.service.IdentityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final IdentityService identityService;

    public UserController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);

        try {
            AuthenticationResponse authenticationResponse = identityService.authenticate(authenticationRequest);

            Cookie cookie = new Cookie("jwtToken", authenticationResponse.getToken());
            cookie.setPath("/");
            // TODO trzeba sie dowiedziec czy mozna tego uzyc w tym przypadku i jak
//        cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 5);
            response.addCookie(cookie);
        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        return "redirect:/";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(HttpServletRequest request, HttpServletResponse response, Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        RegisterRequest registerRequest = new RegisterRequest(username, password);
        try {
            AuthenticationResponse authenticationResponse = identityService.register(registerRequest);

            Cookie cookie = new Cookie("jwtToken", authenticationResponse.getToken());
            cookie.setPath("/");
            // TODO trzeba sie dowiedziec czy mozna tego uzyc w tym przypadku i jak
//        cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 5);
            response.addCookie(cookie);
        } catch (Exception e) {
            model.addAttribute("error", "User already exists");
            return "login";
        }

        return "redirect:/";
    }
}
