package presentation;

import business.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

// This should be in presentation package
@RestController
public class Controller {

    @Autowired
    RecipeService recipeService;

    @Autowired
    UserService userService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @PostMapping("/api/recipe/new")
    public Map<String, Long> saveRecipe(@Valid @RequestBody Recipe recipe) {
        Recipe recipeCreate = recipeService.save(recipe);
        return Collections.singletonMap("id", recipeCreate.getId());
    }

    @PostMapping("/api/register")
    public Object registerUser(@Valid @RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User registered = userService.registerNewUserAccount(user);
        if (registered == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            System.out.println(registered);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PutMapping("/api/recipe/{id}")
    public Object putRecipe(@PathVariable long id, @Valid @RequestBody Recipe recipe) {
        Recipe recipeUpdate = recipeService.update(id, recipe);
        Authentication authentication = authenticationFacade.getAuthentication();
        if (recipeUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!recipeService.findRecipeById(id).getUser().getEmail().equals(authentication.getName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/recipe/{id}")
    public Object getRecipe(@PathVariable long id) {
        Recipe returnRecipe = recipeService.findRecipeById(id);
        if (returnRecipe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(recipeService.findRecipeById(id));
        }
    }

    @GetMapping(value = "/api/recipe/search")
    public Object search(@RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "category", required = false) String category) {
        if (name != null) {
            return recipeService.findByNameContainsIgnoreCaseOrderByDateDesc(name);
        } else if (category != null) {
            return recipeService.findAllRecipesByCategoryIgnoreCaseOrderByDateDesc(category);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable long id) {
        Authentication authentication = authenticationFacade.getAuthentication();
        if (recipeService.findRecipeById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!recipeService.findRecipeById(id).getUser().getEmail().equals(authentication.getName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        recipeService.deleteRecipeById(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
