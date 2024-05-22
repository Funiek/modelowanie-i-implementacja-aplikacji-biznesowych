package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.RecipeDto;
import org.example.request.*;
import org.example.response.AuthenticationResponse;
import org.example.response.UpdateRatingResponse;
import org.example.service.FileService;
import org.example.service.IdentityService;
import org.example.service.RecipesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class WebController {

    private final RecipesService recipesService;
    private final IdentityService identityService;
    private final FileService fileService;
    private final ResourceLoader resourceLoader;

    protected Logger logger = Logger.getLogger(WebController.class.getName());

    public WebController(RecipesService recipesService, IdentityService identityService, FileService fileService, ResourceLoader resourceLoader) {
        this.recipesService = recipesService;
        this.identityService = identityService;
        this.fileService = fileService;
        this.resourceLoader = resourceLoader;
    }


    @RequestMapping("/")
    public String index(Model model) {
        List<RecipeDto> recipeDtos = recipesService.getAll();
        model.addAttribute("recipeDtos", recipeDtos);
        return "index";
    }

    @RequestMapping("/category/{categoryName}")
    public String getByCategory(@PathVariable("categoryName") String categoryName, Model model) {
        List<RecipeDto> recipeDtos = recipesService.getByCategory(categoryName);
        model.addAttribute("recipeDtos", recipeDtos);
        return "index";
    }

    @RequestMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        RecipeDto recipeDto = recipesService.get(id);
        model.addAttribute("recipeDto", recipeDto);
        return "details";
    }

    @RequestMapping(value = "/rate", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public RecipeDto rate(@RequestBody RateRecipeRequest rateRecipeRequest) {
        return recipesService.rate(rateRecipeRequest);
    }

    @RequestMapping(value = "/updaterating", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public UpdateRatingResponse rate(@RequestBody UpdateRatingRequest updateRatingRequest) {
        RecipeDto recipeDto = recipesService.get(updateRatingRequest.getId());

        UpdateRatingResponse updateRatingResponse = new UpdateRatingResponse();
        updateRatingResponse.setRating(recipeDto.getRating());
        updateRatingResponse.setCountVotes(recipeDto.getCountVotes());

        return updateRatingResponse;
    }

    @GetMapping("/manage/create-recipe")
    public String createRecipe(Model model) {
        model.addAttribute("recipeDto", new RecipeDto());
        return "createRecipe";
    }

    @PostMapping("/manage/create-recipe")
    public String createRecipe(@ModelAttribute RecipeDto recipeDto,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               @CookieValue("jwtToken") String jwtToken) throws IOException {
        recipeDto.setRating(0D);
        recipeDto.setCountVotes(0);
        if (!imageFile.isEmpty()) {
            fileService.sendImage(imageFile, recipeDto.getName());
//            byte[] imageBytes = imageFile.getBytes();
//            fileService.saveImage(imageBytes, recipeDto.getName());
        }
        RecipeDto newRecipeDto = recipesService.createRecipe(recipeDto, jwtToken);
        return "redirect:/";
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
//
//    // TODO potem trzeba to przerzucic czytanie img z azurea/cdna/ciul wie
//    @GetMapping("/img/{imageName}")
//    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
//        Resource image = resourceLoader.getResource("classpath:/static/images/" + imageName);
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(image);
//    }
}
