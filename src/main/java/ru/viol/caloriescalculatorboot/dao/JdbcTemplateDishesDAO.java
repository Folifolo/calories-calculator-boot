package ru.viol.caloriescalculatorboot.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.viol.caloriescalculatorboot.dao.mappers.DishMapper;
import ru.viol.caloriescalculatorboot.dao.mappers.IntegerMapper;
import ru.viol.caloriescalculatorboot.dao.mappers.PortionMapper;
import ru.viol.caloriescalculatorboot.models.Dish;
import ru.viol.caloriescalculatorboot.models.IngredientPortion;

@Component
@Qualifier("jdbcDish")
public class JdbcTemplateDishesDAO implements DishesDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateDishesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Object index() {
        return jdbcTemplate.query("SELECT id, name FROM Dish", new BeanPropertyRowMapper<>(Dish.class));
    }

    public Object show(int id) {
        return jdbcTemplate.queryForObject(
                "SELECT d.id as id, d.name as name,\n" +
                        "       d_i.ingredient_id as ingredient_id,\n" +
                        "       i.name as ingredient_name, i.calories as ingredient_calories,\n" +
                        "       d_i.weight as weight\n" +
                        "FROM dish d\n" +
                        "         left join dish_ingredient d_i on d_i.dish_id = d.id\n" +
                        "         left join ingredient i on d_i.ingredient_id = i.id\n" +
                        "WHERE d.id = ?\n" +
                        "ORDER BY d.id",
                new DishMapper(), id);
    }

    public void update(int id, Dish dish) {
        jdbcTemplate.update("UPDATE dish SET name=? WHERE id=?",
                dish.getName(), id);
        jdbcTemplate.update("DELETE FROM dish_ingredient WHERE dish_id=?", id);
        for (IngredientPortion ingredient : dish.getIngredients())
            jdbcTemplate.update("INSERT INTO dish_ingredient(dish_id, ingredient_id, weight) VALUES (?, ?, ?)",
                    id, ingredient.getIngredientId(), ingredient.getWeight());
    }

    public void addIngredient(int id, IngredientPortion ingredientPortion) {
        System.out.println(ingredientPortion);
        jdbcTemplate.update("INSERT INTO dish_ingredient(dish_id, ingredient_id, weight) VALUES (?, ?, ?)",
                id, ingredientPortion.getIngredientId(), ingredientPortion.getWeight());
    }

    public void save(Dish dish) {
        System.out.println(dish);
        int id = jdbcTemplate.queryForObject("INSERT INTO dish(name) VALUES(?) RETURNING id",
                new IntegerMapper(), dish.getName());
        for (IngredientPortion ingredient : dish.getIngredients())
            jdbcTemplate.update("INSERT INTO dish_ingredient(dish_id, ingredient_id, weight) VALUES (?, ?, ?)",
                    id, ingredient.getIngredientId(), ingredient.getWeight());
    }

    public Object showPortionByIngredientId(int did, int iid) {
        return jdbcTemplate.queryForObject(
                "SELECT d_i.ingredient_id as id,\n" +
                        "       i.name as name, i.calories as calories,\n" +
                        "       d_i.weight as weight\n" +
                        "FROM dish d\n" +
                        "         left join dish_ingredient d_i on d_i.dish_id = d.id\n" +
                        "         left join ingredient i on d_i.ingredient_id = i.id\n" +
                        "WHERE d.id = ? AND i.id = ?\n" +
                        "ORDER BY d.id",
                new PortionMapper(), did, iid);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM dish_ingredient WHERE dish_id=?", id);
        jdbcTemplate.update("DELETE FROM dish WHERE id=?", id);

    }

    public void updateName(int id, Dish dish) {
        jdbcTemplate.update("UPDATE dish SET name=? WHERE id=?",
                dish.getName(), id);
    }
}
