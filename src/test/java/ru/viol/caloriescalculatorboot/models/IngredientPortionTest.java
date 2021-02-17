package ru.viol.caloriescalculatorboot.models;

import org.junit.Assert;
import org.junit.Test;

public class IngredientPortionTest {
    private final double EPS = 1e-9;


    @Test
    public void caloriesShouldBeWeightAndIngredientCaloriesMultiplication() {
        IngredientPortion ingredientPortion = new IngredientPortion();
        Ingredient ingredient = new Ingredient();
        ingredient.setCalories(200);
        ingredientPortion.setIngredient(ingredient);
        ingredientPortion.setWeight(50);

        double calories = ingredientPortion.getCalories();

        Assert.assertEquals(100, calories, EPS);
    }
}
