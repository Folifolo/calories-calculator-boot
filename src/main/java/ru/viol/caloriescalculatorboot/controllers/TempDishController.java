package ru.viol.caloriescalculatorboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.viol.caloriescalculatorboot.dao.DishesDAO;
import ru.viol.caloriescalculatorboot.dao.IngredientsDAO;
import ru.viol.caloriescalculatorboot.models.Dish;
import ru.viol.caloriescalculatorboot.models.DishPortion;

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
        return modelAndView;
    }

    @GetMapping("/temp")
    public ModelAndView showTemp(@ModelAttribute("dishPortion") DishPortion dishPortion) {
        ModelAndView modelAndView = new ModelAndView("dishes/show");
        modelAndView.addObject("dish", tmpDish);
        return modelAndView;
    }

    @PatchMapping("/temp")
    public ModelAndView updateTemp(@ModelAttribute("dish") Dish dish) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/temp");
        tmpDish.copyWeights(dish);
        return modelAndView;
    }

    @DeleteMapping("/temp/{id}")
    public ModelAndView deleteTempIngredient(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/new");
        tmpDish.deleteIngredientPortionByIngredientId(id);
        return modelAndView;
    }

    @PatchMapping("/temp/weight")
    public ModelAndView updateTempWeight(@ModelAttribute("dish") Dish dish) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/temp");
        if (dish.getCookedWeight() <= tmpDish.getRawWeight())
            tmpDish.setCookedWeight(dish.getCookedWeight());
        return modelAndView;
    }
}
