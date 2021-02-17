package ru.viol.caloriescalculatorboot.dao.mappers;


import org.springframework.jdbc.core.RowMapper;
import ru.viol.caloriescalculatorboot.models.Dish;
import ru.viol.caloriescalculatorboot.models.Ingredient;
import ru.viol.caloriescalculatorboot.models.IngredientPortion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishMapper implements RowMapper<Dish> {
    @Override
    public Dish mapRow(ResultSet resultSet, int i) throws SQLException {
        Dish dish = new Dish();
        List<IngredientPortion> ingredients = new ArrayList<>();

        dish.setId(resultSet.getInt("id"));
        dish.setName(resultSet.getString("name"));

        Ingredient ingredient_first = new Ingredient();
        ingredient_first.setId(resultSet.getInt("ingredient_id"));
        ingredient_first.setName(resultSet.getString("ingredient_name"));
        ingredient_first.setCalories(resultSet.getInt("ingredient_calories"));

        ingredients.add(new IngredientPortion(ingredient_first, resultSet.getInt("weight")));

        while (resultSet.next()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(resultSet.getInt("ingredient_id"));
            ingredient.setName(resultSet.getString("ingredient_name"));
            ingredient.setCalories(resultSet.getInt("ingredient_calories"));

            ingredients.add(new IngredientPortion(ingredient, resultSet.getInt("weight")));
        }

        dish.setIngredients(ingredients);
        return dish;
    }
}