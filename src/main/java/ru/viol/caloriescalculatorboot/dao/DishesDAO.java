package ru.viol.caloriescalculatorboot.dao;


import org.springframework.stereotype.Component;
import ru.viol.caloriescalculatorboot.models.Dish;
import ru.viol.caloriescalculatorboot.models.IngredientPortion;

@Component
public interface DishesDAO {

    public Object index();

    public Object show(int id);

    public void update(int id, Dish dish);

    public void addIngredient(int id, IngredientPortion ingredientPortion);

    public void save(Dish dish);

    public Object showPortionByIngredientId(int did, int iid);

    public void delete(int id);

    public void updateName(int id, Dish dish);
}
