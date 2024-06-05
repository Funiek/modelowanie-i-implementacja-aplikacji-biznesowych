package org.example.controller;

import org.example.dto.RecipeDto;
import org.example.request.*;
import org.example.service.ImageService;
import org.example.service.RecipesService;
import org.springframework.core.io.Resource;
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
    private final ImageService imageService;
    protected Logger logger = Logger.getLogger(WebController.class.getName());

    public WebController(RecipesService recipesService, ImageService imageService) {
        this.recipesService = recipesService;
        this.imageService = imageService;
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

    @RequestMapping("/recipes/details/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        RecipeDto recipeDto = recipesService.get(id);
        model.addAttribute("recipeDto", recipeDto);
        return "details";
    }

    @RequestMapping(value = "/recipes/rate", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public RecipeDto rate(@RequestBody VotesSaveRequest votesSaveRequest) {
        return recipesService.rate(votesSaveRequest);
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
        recipesService.createRecipe(recipeDto, jwtToken);

        return "redirect:/";
    }



    @GetMapping("/img/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable("imageName") String imageName) {
        logger.info("ZDJECIE DO POBRANIA: " + imageName);
        return imageService.getImage(imageName);
    }
}
