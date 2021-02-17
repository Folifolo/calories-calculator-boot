package ru.viol.caloriescalculatorboot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.viol.caloriescalculatorboot.models.Ingredient;


@Component
@Qualifier("jdbcTemplateIngredient")
public class JdbcTemplateIngredientsDAO implements  IngredientsDAO{


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateIngredientsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Object index() {
        return jdbcTemplate.query("SELECT * FROM Ingredient", new BeanPropertyRowMapper<>(Ingredient.class));
    }

    @Override
    public Object show(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM Ingredient WHERE id=?",
                new BeanPropertyRowMapper<>(Ingredient.class), id);
    }

    @Override
    public void save(Ingredient ingredient) {
        jdbcTemplate.update("INSERT INTO ingredient(name, calories) VALUES(?, ?)",
                ingredient.getName(), ingredient.getCalories());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM ingredient WHERE id=?", id);
    }

    @Override
    public void update(int id, Ingredient ingredient) {
        jdbcTemplate.update("UPDATE ingredient SET name=?, calories=? WHERE id=?",
                ingredient.getName(), ingredient.getCalories(), id);
    }
}
