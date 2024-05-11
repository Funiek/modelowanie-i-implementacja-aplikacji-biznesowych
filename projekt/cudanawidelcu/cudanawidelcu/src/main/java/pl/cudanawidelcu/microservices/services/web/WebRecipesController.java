package pl.cudanawidelcu.microservices.services.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.cudanawidelcu.microservices.services.web.dto.RecipeDto;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class WebRecipesController {
    @Autowired
    protected WebRecipesService recipesService;

    protected Logger logger = Logger.getLogger(WebRecipesController.class.getName());

    public WebRecipesController(WebRecipesService recipesService) {
        this.recipesService = recipesService;
    }

    @RequestMapping("/")
    public String home(Model model) {
        List<RecipeDto> recipeDtos = recipesService.getAll();
        logger.info("AAAAAAAAAAAAAAAAAAAAAAAAAAA: "+recipeDtos.size());
        model.addAttribute("recipeDtos", recipeDtos);

        return "index";
    }
}
