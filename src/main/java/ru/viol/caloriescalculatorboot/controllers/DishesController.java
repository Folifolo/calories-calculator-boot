package ru.viol.caloriescalculatorboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.viol.caloriescalculatorboot.dao.DishesDAO;
import ru.viol.caloriescalculatorboot.dao.IngredientsDAO;
import ru.viol.caloriescalculatorboot.models.Dish;
import ru.viol.caloriescalculatorboot.models.Ingredient;
import ru.viol.caloriescalculatorboot.models.IngredientPortion;

import javax.validation.Valid;

@RestController
@RequestMapping("/dishes")
public class DishesController {

    private final DishesDAO dishesDAO;
    private final IngredientsDAO ingredientsDAO;

    private Dish tmpDish;

    @Autowired
    public DishesController(@Qualifier("hibernateDish") DishesDAO dishesDAO, @Qualifier("hibernateIngredient") IngredientsDAO ingredientsDAO) {
        this.dishesDAO = dishesDAO;
        this.ingredientsDAO = ingredientsDAO;
    }



    @GetMapping()
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("dishes/index");
        modelAndView.addObject("dishes", dishesDAO.index());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView show(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/temp");
        tmpDish = (Dish)dishesDAO.show(id);
        System.out.println(tmpDish);
        return modelAndView;
    }

    @GetMapping("/temp")
    public ModelAndView showTemp() {
        ModelAndView modelAndView = new ModelAndView("dishes/show");
        modelAndView.addObject("dish", tmpDish);
        return modelAndView;
    }

    @PostMapping("/{id}/ingredient")
    public ModelAndView addIngredient(@ModelAttribute("ingredientPortion") IngredientPortion ingredientPortion,
                                @PathVariable("id") int id) {
        IngredientPortion tmp = new IngredientPortion();
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/"+ id +"/update");
        Dish dish = (Dish)dishesDAO.show(id);
        tmp.setWeight(ingredientPortion.getWeight());
        tmp.setIngredient((Ingredient)ingredientsDAO.show(ingredientPortion.getIngredientId()));
        tmp.setDish(dish);
        dishesDAO.addIngredient(dish.getId(), tmp);
        //dishesDAO.update(id, dish);
        return modelAndView;
    }

    @DeleteMapping("/{did}/{iid}")
    public ModelAndView deleteIngredient(@PathVariable("did") int did, @PathVariable("iid") int iid) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/"+ did+ "/update");
        Dish dish = (Dish)dishesDAO.show(did);
        dish.deleteIngredient(iid);
        dishesDAO.update(did, dish);
        return modelAndView;
    }

    @PatchMapping("/temp")
    public ModelAndView updateTemp(@ModelAttribute("dish") Dish dish) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/temp");
        tmpDish.copyWeights(dish);
        return modelAndView;
    }


    @PatchMapping("/{id}")
    public ModelAndView updateName(@ModelAttribute("dish") Dish dish,@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/"+id+"/update");

        dishesDAO.updateName(id, dish);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView eraseTemp() {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/new");
        tmpDish = new Dish();
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView newDish(@ModelAttribute("ingredientPortion") IngredientPortion ingredientPortion) {
        ModelAndView modelAndView = new ModelAndView("dishes/new");
        modelAndView.addObject("dish", tmpDish);
        modelAndView.addObject("ingredients", ingredientsDAO.index());
        return modelAndView;
    }

    @PostMapping()
    public ModelAndView create(@ModelAttribute("dish") Dish dish) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes");
        for(IngredientPortion ingredientPortion : dish.getIngredients()) {
            ingredientPortion.setDish(dish);
            ingredientPortion.setIngredient((Ingredient) ingredientsDAO.show(ingredientPortion.getIngredientId()));
        }
        dishesDAO.save(dish);
        return modelAndView;
    }

    @PostMapping("/temp/ingredient")
    public ModelAndView addTmpIngredient(@ModelAttribute("ingredientPortion") IngredientPortion ingredientPortion) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/new");
        Ingredient ingredient = (Ingredient) ingredientsDAO.show(ingredientPortion.getIngredientId());
        ingredientPortion.setIngredient(ingredient);
        tmpDish.addIngredient(ingredientPortion);
        System.out.println(ingredientPortion);
        return modelAndView;
    }

    @PatchMapping("/{id}/ingredient")
    public ModelAndView updateIngredient(@ModelAttribute("ingredientP") IngredientPortion ingredientPortion,
                                @PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/{id}/update");
        System.out.println(ingredientPortion);
        Dish dish = (Dish)dishesDAO.show(id);
        System.out.println(123);
        dish.updateIngredientWeight(ingredientPortion);
        dishesDAO.update(id, dish);
        System.out.println(456);
        return modelAndView;
    }

    @DeleteMapping("/temp/{id}")
    public ModelAndView deleteTmpIngredient(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/new");
        tmpDish.deleteIngredient(id);
        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes");
        dishesDAO.delete(id);
        return modelAndView;
    }

    @GetMapping("/{id}/update")
    public ModelAndView update(@ModelAttribute("ingredientPortion") IngredientPortion ingredientPortion,
                         @PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("dishes/update");
        modelAndView.addObject("dish", dishesDAO.show(id));
        modelAndView.addObject("ingredients", ingredientsDAO.index());
        return modelAndView;
    }

}
