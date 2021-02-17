package ru.viol.caloriescalculatorboot.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.viol.caloriescalculatorboot.models.Ingredient;
import ru.viol.caloriescalculatorboot.models.IngredientPortion;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PortionMapper implements RowMapper<IngredientPortion> {
    @Override
    public IngredientPortion mapRow(ResultSet resultSet, int i) throws SQLException {
        IngredientPortion portion = new IngredientPortion();
        Ingredient ingredient = new Ingredient();

        ingredient.setId(resultSet.getInt("id"));
        ingredient.setName(resultSet.getString("name"));
        ingredient.setCalories(resultSet.getInt("calories"));
        portion.setIngredient(ingredient);
        portion.setWeight(resultSet.getInt("weight"));

        return portion;
    }
}
