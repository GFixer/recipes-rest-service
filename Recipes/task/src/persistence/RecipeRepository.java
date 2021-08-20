package persistence;


import org.springframework.data.repository.CrudRepository;
import business.Recipe;

import java.util.List;


// This should be in persistence package
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    Recipe findRecipeById(Long id);

    List<Recipe> findAllRecipesByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<Recipe> findByNameContainsIgnoreCaseOrderByDateDesc(String name);

    List<Recipe> findAll();

    void deleteById(Long id);
}
