package business;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import persistence.RecipeRepository;

import java.util.List;

// This should be in business package
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Autowired
    UserService userService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    public Recipe findRecipeById(Long id) {
        return recipeRepository.findRecipeById(id);
    }

    public List<Recipe> findAllRecipesByCategoryIgnoreCaseOrderByDateDesc(String category) {
        return recipeRepository.findAllRecipesByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> findByNameContainsIgnoreCaseOrderByDateDesc(String name) {
        return recipeRepository.findByNameContainsIgnoreCaseOrderByDateDesc(name);
    }

    public Recipe save(Recipe toSave) {
        Authentication authentication = authenticationFacade.getAuthentication();
        toSave.setUser(userService.findUserByEmail(authentication.getName()));
        return recipeRepository.save(toSave);
    }

    public Recipe update(long id, Recipe toUpdate) {
        Recipe recipe = recipeRepository.findRecipeById(id);
        if (recipe != null) {
            recipe.setName(toUpdate.getName());
            recipe.setDescription(toUpdate.getDescription());
            recipe.setIngredients(toUpdate.getIngredients());
            recipe.setDirections(toUpdate.getDirections());
            recipe.setCategory(toUpdate.getCategory());
            recipe.setDate();
            recipeRepository.save(recipe);
        }
        return recipe;
    }

    public void deleteRecipeById(Long id) {
        recipeRepository.deleteById(id);
    }

    public List findAll() {
        return recipeRepository.findAll();
    }
}
