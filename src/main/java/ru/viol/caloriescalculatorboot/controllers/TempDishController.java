package ru.viol.caloriescalculatorboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.viol.caloriescalculatorboot.dao.DishesDAO;
import ru.viol.caloriescalculatorboot.dao.IngredientsDAO;
import ru.viol.caloriescalculatorboot.models.Dish;
import ru.viol.caloriescalculatorboot.models.DishPortion;
import ru.viol.caloriescalculatorboot.models.Ingredient;
import ru.viol.caloriescalculatorboot.models.IngredientPortion;

@RestController
@RequestMapping("/dishes")
public class TempDishController {

    private final DishesDAO dishesDAO;
    private final IngredientsDAO ingredientsDAO;

    private Dish tmpDish;

    @Autowired
    public TempDishController(@Qualifier("hibernateDish") DishesDAO dishesDAO,
                            @Qualifier("hibernateIngredient") IngredientsDAO ingredientsDAO) {
        this.dishesDAO = dishesDAO;
        this.ingredientsDAO = ingredientsDAO;
    }

    @GetMapping("/{id}")
    public ModelAndView show(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/temp");
        tmpDish = (Dish) dishesDAO.show(id);
        System.out.println(tmpDish);
        return modelAndView;
    }

    @GetMapping("/temp")
    public ModelAndView showTemp(@ModelAttribute("dishPortion") DishPortion dishPortion) {
        ModelAndView modelAndView = new ModelAndView("dishes/show");
        modelAndView.addObject("dish", tmpDish);
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

    @PatchMapping("/temp")
    public ModelAndView updateTemp(@ModelAttribute("dish") Dish dish) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/temp");
        tmpDish.copyWeights(dish);
        return modelAndView;
    }

    @DeleteMapping("/temp/{id}")
    public ModelAndView deleteTmpIngredient(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/new");
        tmpDish.deleteIngredient(id);
        return modelAndView;
    }

    @PatchMapping("/temp/weight")
    public ModelAndView updateTempWeight(@ModelAttribute("dish") Dish dish) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/temp");
        if(dish.getCookedWeight() <= tmpDish.getRawWeight())
            tmpDish.setCookedWeight(dish.getCookedWeight());
        return modelAndView;
    }
}
