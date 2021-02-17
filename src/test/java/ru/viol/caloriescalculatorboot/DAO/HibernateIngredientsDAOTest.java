package ru.viol.caloriescalculatorboot.DAO;

import org.junit.Assert;
import org.junit.Test;
import ru.viol.caloriescalculatorboot.dao.HibernateIngredientsDAO;
import ru.viol.caloriescalculatorboot.dao.IngredientsDAO;
import ru.viol.caloriescalculatorboot.models.Ingredient;

import java.util.List;

public class HibernateIngredientsDAOTest {
    private final IngredientsDAO ingredientsDAO = new HibernateIngredientsDAO();

    private Ingredient createTestIngredient() {
        int INGREDIENT_CALORIES = 100;
        String INGREDIENT_NAME = "name";
        Ingredient ingredient = new Ingredient();
        ingredient.setName(INGREDIENT_NAME);
        ingredient.setCalories(INGREDIENT_CALORIES);
        return ingredient;
    }

    @Test
    public void showShouldReturnCorrectIngredientId() {
        int ID = 1;
        Ingredient ingredient = (Ingredient)ingredientsDAO.show(ID);

        Assert.assertEquals(ID, ingredient.getId());
    }



    @Test
    public void saveShouldSaveCorrectIngredient() {
        Ingredient ingredient = createTestIngredient();

        ingredientsDAO.save(ingredient);

        Ingredient ingredient1 = (Ingredient) ingredientsDAO.show(ingredient.getId());
        Assert.assertEquals(ingredient, ingredient1);

        ingredientsDAO.delete(ingredient.getId());
    }

    @Test
    public void deleteShouldDeleteIngredient() {
        Ingredient ingredient = createTestIngredient();
        ingredientsDAO.save(ingredient);

        ingredientsDAO.delete(ingredient.getId());

        Assert.assertNull(ingredientsDAO.show(ingredient.getId()));
    }



    @Test
    public void indexReturnListOfCorrectSize() {
        List<Ingredient> list = (List<Ingredient>)ingredientsDAO.index();

        Assert.assertEquals(3, list.size());
    }

    @Test
    public void updateShouldUpdate() {
        final String ANOTHER_NAME = "another_name";
        Ingredient ingredient = createTestIngredient();
        ingredientsDAO.save(ingredient);
        ingredient.setName(ANOTHER_NAME);

        ingredientsDAO.update(ingredient.getId(), ingredient);

        ingredient = (Ingredient) ingredientsDAO.show(ingredient.getId());
        Assert.assertEquals(ANOTHER_NAME, ingredient.getName());

        ingredientsDAO.delete(ingredient.getId());
    }

}
