package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.request.IdentityAuthenticateRequest;
import org.example.request.IdentityRegisterRequest;
import org.example.response.IdentityAuthenticateResponse;
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
        IdentityAuthenticateRequest identityAuthenticateRequest = new IdentityAuthenticateRequest(username, password);

        try {
            IdentityAuthenticateResponse identityAuthenticateResponse = identityService.authenticate(identityAuthenticateRequest);
            int cookieTime = identityService.getTimeForCookie(identityAuthenticateResponse.getToken());

            Cookie cookie = new Cookie("jwtToken", identityAuthenticateResponse.getToken());
            cookie.setPath("/");
            cookie.setMaxAge(cookieTime);
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
        IdentityRegisterRequest identityRegisterRequest = new IdentityRegisterRequest(username, password);
        try {
            IdentityAuthenticateResponse identityAuthenticateResponse = identityService.register(identityRegisterRequest);
            int cookieTime = identityService.getTimeForCookie(identityAuthenticateResponse.getToken());

            Cookie cookie = new Cookie("jwtToken", identityAuthenticateResponse.getToken());
            cookie.setPath("/");
            cookie.setMaxAge(cookieTime);
            response.addCookie(cookie);
        } catch (Exception e) {
            model.addAttribute("error", "User already exists");
            return "login";
        }

        return "redirect:/";
    }
}
