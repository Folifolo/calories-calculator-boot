package ru.viol.caloriescalculatorboot.DAO;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import ru.viol.caloriescalculatorboot.config.HibernateUtil;
import ru.viol.caloriescalculatorboot.dao.DishesDAO;
import ru.viol.caloriescalculatorboot.dao.HibernateDishesDAO;
import ru.viol.caloriescalculatorboot.dao.HibernateIngredientsDAO;
import ru.viol.caloriescalculatorboot.dao.IngredientsDAO;
import ru.viol.caloriescalculatorboot.models.Dish;
import ru.viol.caloriescalculatorboot.models.Ingredient;
import ru.viol.caloriescalculatorboot.models.IngredientPortion;

import java.util.ArrayList;
import java.util.List;

public class HibernateDishesDAOTest {
    DishesDAO dishesDAO = new HibernateDishesDAO();
    IngredientsDAO ingredientsDAO = new HibernateIngredientsDAO();


    @Test
    public void showShouldReturnCorrectDishId() {
        int ID = 1;
        Dish dish = (Dish)dishesDAO.show(ID);

        Assert.assertEquals(ID, dish.getId());
    }

    @Test
    public void showShouldReturnIngredientList() {
        int ID = 1;
        Dish dish = (Dish)dishesDAO.show(ID);

        Assert.assertNotNull(dish.getIngredients());
    }

    @Test
    public void saveShouldSaveCorrectDishWithoutIngredients() {
        Dish dish = new Dish();
        dish.setName("name");


        dishesDAO.save(dish);

        Dish dish1 = (Dish) dishesDAO.show(dish.getId());
        Assert.assertEquals(dish, dish1);

        dishesDAO.delete(dish.getId());
    }

    @Test
    public void saveShouldSaveCorrectDishWithIngredients() {
        Dish dish = new Dish();
        dish.setName("name");

        IngredientPortion ingredientPortion1 = generateIngredientPortion(1, 500, dish);
        dish.addIngredient(ingredientPortion1);

        IngredientPortion ingredientPortion2 = generateIngredientPortion(2, 300, dish);
        dish.addIngredient(ingredientPortion2);

        dishesDAO.save(dish);

        Dish dish1 = (Dish) dishesDAO.show(dish.getId());
        Assert.assertEquals(dish, dish1);

        dishesDAO.delete(dish.getId());
    }

    @Test
    public void deleteShouldDeleteDishWithoutIngredients() {
        Dish dish = new Dish();
        dish.setName("name");
        dishesDAO.save(dish);

        dishesDAO.delete(dish.getId());

        Assert.assertNull(dishesDAO.show(dish.getId()));
    }

    @Test
    public void deleteShouldDeleteDishWithIngredients() {
        Dish dish = new Dish();
        dish.setName("name");

        IngredientPortion ingredientPortion1 = generateIngredientPortion(1, 500, dish);
        dish.addIngredient(ingredientPortion1);

        IngredientPortion ingredientPortion2 = generateIngredientPortion(2, 300, dish);
        dish.addIngredient(ingredientPortion2);

        dishesDAO.save(dish);

        dishesDAO.delete(dish.getId());

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        IngredientPortion ingredientPortion = session.get(IngredientPortion.class, ingredientPortion1.getId());
        session.getTransaction().commit();
        session.close();

        Assert.assertNull(ingredientPortion);
    }


    @Test
    public void indexReturnListOfCorrectSize() {
        List<Dish> list = (List<Dish>)dishesDAO.index();

        Assert.assertEquals(1, list.size());
    }

    @Test
    public void addIngredientShouldAddIngredient() {
        Dish dish = new Dish();
        dish.setName("name");
        dishesDAO.save(dish);

        IngredientPortion ingredientPortion = generateIngredientPortion(1, 500, dish);
        dishesDAO.addIngredient(dish.getId(), ingredientPortion);

        Dish dish1 = (Dish) dishesDAO.show(dish.getId());
        IngredientPortion ingredientPortion1 = dish1.getIngredientByIngredientId(ingredientPortion.getIngredientId());

        Assert.assertEquals(ingredientPortion, ingredientPortion1);

        dishesDAO.delete(dish.getId());
    }

    @Test
    public void addIngredientShouldAddIngredientToLoadedDish() {
        Dish dish = new Dish();
        dish.setName("name");

        IngredientPortion ingredientPortion1 = generateIngredientPortion(1, 500, dish);
        dish.addIngredient(ingredientPortion1);

        dishesDAO.save(dish);

        dish = (Dish)dishesDAO.show(dish.getId());
        IngredientPortion ingredientPortion2 = generateIngredientPortion(2, 300, dish);
        dishesDAO.addIngredient(dish.getId(), ingredientPortion2);

        Dish dish1 = (Dish) dishesDAO.show(dish.getId());
        IngredientPortion ingredientPortion = dish1.getIngredientByIngredientId(ingredientPortion2.getIngredientId());

        Assert.assertEquals(ingredientPortion2, ingredientPortion);

        dishesDAO.delete(dish.getId());
    }

    @Test
    public void showPortionShouldReturnCorrectIngredientPortion() {
        Dish dish = new Dish();
        dish.setName("name");

        IngredientPortion ingredientPortion1 = generateIngredientPortion(1, 500, dish);
        dish.addIngredient(ingredientPortion1);

        IngredientPortion ingredientPortion2 = generateIngredientPortion(2, 300, dish);
        dish.addIngredient(ingredientPortion2);

        dishesDAO.save(dish);

        IngredientPortion ingredientPortion = (IngredientPortion)
                dishesDAO.showPortionByIngredientId(dish.getId(), ingredientPortion1.getIngredientId());

        Assert.assertEquals(ingredientPortion1, ingredientPortion);

        dishesDAO.delete(dish.getId());
    }

    @Test
    public void updateShouldUpdateName () {
        final String newName = "another name";
        Dish dish = new Dish();
        dish.setName("name");
        dishesDAO.save(dish);

        dish.setName(newName);

        dishesDAO.update(dish.getId(), dish);

        Dish dish1 = (Dish) dishesDAO.show(dish.getId());

        Assert.assertEquals(newName, dish1.getName());

        dishesDAO.delete(dish1.getId());
    }

    @Test
    public void updateShouldUpdateIngredientsWeight () {
        Dish dish = new Dish();
        dish.setName("name");

        IngredientPortion ingredientPortion1 = generateIngredientPortion(1, 500, dish);
        dish.addIngredient(ingredientPortion1);

        IngredientPortion ingredientPortion2 = generateIngredientPortion(2, 300, dish);
        dish.addIngredient(ingredientPortion2);

        dishesDAO.save(dish);

        ingredientPortion1.setWeight(10);

        dishesDAO.update(dish.getId(), dish);

        Dish dish1 = (Dish) dishesDAO.show(dish.getId());
        Assert.assertEquals(dish, dish1);
        System.out.println(dish);

        dishesDAO.delete(dish.getId());
    }

    @Test
    public void updateShouldUpdateIngredientsList () {
        Dish dish = new Dish();
        dish.setName("name");
        dishesDAO.save(dish);

        IngredientPortion ingredientPortion1 = generateIngredientPortion(1, 500, dish);
        dish.addIngredient(ingredientPortion1);

        dishesDAO.update(dish.getId(), dish);

        Dish dish1 = (Dish) dishesDAO.show(dish.getId());
        Assert.assertEquals(dish, dish1);
        System.out.println(dish);

        dishesDAO.delete(dish.getId());
    }

    private IngredientPortion generateIngredientPortion(int ingredientId, int weight, Dish dish) {
        Ingredient ingredient = (Ingredient) ingredientsDAO.show(ingredientId);
        IngredientPortion ingredientPortion = new IngredientPortion(ingredient, weight);
        ingredientPortion.setDish(dish);
        return ingredientPortion;
    }

    private Dish createTestDish() {
        Dish dish = new Dish();
        dish.setName("name");

        IngredientPortion ingredientPortion1 = generateIngredientPortion(1, 500, dish);
        dish.addIngredient(ingredientPortion1);

        IngredientPortion ingredientPortion2 = generateIngredientPortion(2, 300, dish);
        dish.addIngredient(ingredientPortion2);

        return dish;
    }

}
