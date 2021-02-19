package ru.viol.caloriescalculatorboot.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.viol.caloriescalculatorboot.dao.DishesDAO;
import ru.viol.caloriescalculatorboot.dao.IngredientsDAO;
import ru.viol.caloriescalculatorboot.models.Dish;
import ru.viol.caloriescalculatorboot.models.Ingredient;

import javax.validation.Valid;

@Controller
@RequestMapping("/ingredients")
public class IngredientsController {

    private final IngredientsDAO ingredientsDAO;
    private final DishesDAO dishesDAO;

    @Autowired
    public IngredientsController(@Qualifier("hibernateIngredient") IngredientsDAO ingredientsDAO,
                                 @Qualifier("hibernateDish") DishesDAO dishesDAO) {
        this.ingredientsDAO = ingredientsDAO;
        this.dishesDAO = dishesDAO;
    }

    @GetMapping()
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("ingredients/index");
        modelAndView.addObject("ingredients", ingredientsDAO.index());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView show(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("ingredients/show");
        modelAndView.addObject("ingredient", ingredientsDAO.show(id));
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView newIngredient(@ModelAttribute("ingredient") Ingredient ingredient) {
        ModelAndView modelAndView = new ModelAndView("ingredients/new");
        return modelAndView;
    }

    @PostMapping()
    public ModelAndView create(@ModelAttribute("ingredient") @Valid Ingredient ingredient,
                               BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("redirect:/ingredients");
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("ingredients/new");
            return modelAndView;
        }
        ingredientsDAO.save(ingredient);
        return modelAndView;
    }

    @PatchMapping("/{id}")
    public ModelAndView update(@ModelAttribute("ingredient") @Valid Ingredient ingredient,
                               BindingResult bindingResult, @PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/ingredients");
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("ingredients/" + id);
            return modelAndView;
        }
        ingredientsDAO.update(id, ingredient);
        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/ingredients");
        ingredientsDAO.delete(id);
        return modelAndView;
    }

    @PostMapping("/dish")
    public ModelAndView createFromDish(@ModelAttribute("dish") Dish dish) {
        ModelAndView modelAndView = new ModelAndView("redirect:/ingredients");
        Ingredient ingredient = new Ingredient((Dish) dishesDAO.show(dish.getId()));

        ingredientsDAO.save(ingredient);
        return modelAndView;
    }

}
