package ru.viol.caloriescalculatorboot.models;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
public class DishTest {
    private final double EPS = 1e-9;

    @Test
    public void caloriesShouldBeEqualToIngredientCalories() {
        Dish dish = new Dish();
        List<IngredientPortion> ingredientPortionList = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setCalories(200);
        ingredientPortionList.add(new IngredientPortion(ingredient, 50));
        dish.setIngredients(ingredientPortionList);

        double calories = dish.getCalories();

        Assert.assertEquals(100, calories, EPS);
    }

    @Test
    public void caloriesShouldBeSumOfIngredientsCalories() {
        Dish dish = new Dish();
        List<IngredientPortion> ingredientPortionList = new ArrayList<>();

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setCalories(200);
        ingredientPortionList.add(new IngredientPortion(ingredient1, 50));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setCalories(500);
        ingredientPortionList.add(new IngredientPortion(ingredient2, 100));

        dish.setIngredients(ingredientPortionList);

        double calories = dish.getCalories();

        Assert.assertEquals(600, calories, EPS);
    }

    @Test
    public void canAddIngredientPortion() {
        Dish dish = new Dish();

        Ingredient ingredient = new Ingredient();
        ingredient.setCalories(200);
        ingredient.setId(1);
        ingredient.setName("test");
        IngredientPortion ingredientPortion = new IngredientPortion(ingredient, 50);
        dish.addIngredient(ingredientPortion);

        IngredientPortion returnedIngredientPortion = dish.getIngredients().get(0);

        Assert.assertEquals(ingredientPortion, returnedIngredientPortion);

    }

    @Test
    public void differentIngredientsWeightsShouldNotBeSummed() {
        Dish dish = new Dish();

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setCalories(100);
        ingredient1.setId(1);
        ingredient1.setName("test1");
        IngredientPortion ingredientPortion1 = new IngredientPortion(ingredient1, 100);
        dish.addIngredient(ingredientPortion1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setCalories(200);
        ingredient2.setId(2);
        ingredient2.setName("test2");
        IngredientPortion ingredientPortion2 = new IngredientPortion(ingredient2, 200);
        dish.addIngredient(ingredientPortion2);

        IngredientPortion returnedIngredientPortion1 = dish.getIngredients().get(0);
        IngredientPortion returnedIngredientPortion2 = dish.getIngredients().get(1);


        Assert.assertNotEquals(returnedIngredientPortion1, returnedIngredientPortion2);

    }

    @Test
    public void sameIngredientsWeightsShouldBeSummed() {
        Dish dish = new Dish();

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setCalories(200);
        ingredient1.setId(1);
        ingredient1.setName("test");
        IngredientPortion ingredientPortion1 = new IngredientPortion(ingredient1, 50);
        IngredientPortion ingredientPortion2 = new IngredientPortion(ingredient1, 50);

        dish.addIngredient(ingredientPortion1);
        dish.addIngredient(ingredientPortion2);

        IngredientPortion returnedIngredientPortion = dish.getIngredients().get(0);
        int weight = returnedIngredientPortion.getWeight();
        Assert.assertEquals(100, weight);

    }

    @Test
    public void canDeleteIngredientPortion() {
        int INGREDIENT_ID = 1;
        Dish dish = new Dish();

        Ingredient ingredient = new Ingredient();
        ingredient.setCalories(200);
        ingredient.setId(INGREDIENT_ID);
        ingredient.setName("test");
        IngredientPortion ingredientPortion = new IngredientPortion(ingredient, 50);
        dish.addIngredient(ingredientPortion);

        dish.deleteIngredientPortionByIngredientId(INGREDIENT_ID);

        int ingredientsSize = dish.getIngredients().size();
        Assert.assertEquals(0, ingredientsSize);

    }
}
