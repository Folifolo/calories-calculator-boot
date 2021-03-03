package ru.viol.caloriescalculatorboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.viol.caloriescalculatorboot.dao.interfaces.DishesDAO;
import ru.viol.caloriescalculatorboot.dao.interfaces.IngredientsDAO;
import ru.viol.caloriescalculatorboot.models.Dish;
import ru.viol.caloriescalculatorboot.models.Ingredient;
import ru.viol.caloriescalculatorboot.models.IngredientPortion;

import javax.validation.Valid;

@RestController
@RequestMapping("/dishes")
public class DishesController {

    private final DishesDAO dishesDAO;
    private final IngredientsDAO ingredientsDAO;

    @Autowired
    public DishesController(@Qualifier("hibernateDish") DishesDAO dishesDAO,
                            @Qualifier("hibernateIngredient") IngredientsDAO ingredientsDAO) {
        this.dishesDAO = dishesDAO;
        this.ingredientsDAO = ingredientsDAO;
    }


    @GetMapping()
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("dishes/index");
        modelAndView.addObject("dishes", dishesDAO.index());
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

    @GetMapping("/new")
    public ModelAndView newDish(@ModelAttribute("ingredientPortion") IngredientPortion ingredientPortion,
                                @ModelAttribute("dish") Dish dish) {
        ModelAndView modelAndView = new ModelAndView("dishes/new");

        modelAndView.addObject("ingredients", ingredientsDAO.index());
        return modelAndView;
    }

    @PostMapping("/{id}/ingredient")
    public ModelAndView addIngredient(@ModelAttribute("ingredientPortion") @Valid IngredientPortion ingredientPortion,
                                      BindingResult bindingResult, @PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/" + id + "/update");
        if (bindingResult.hasErrors()) {
            modelAndView = update(ingredientPortion, id);
            return modelAndView;
        }
        IngredientPortion newIngredientPortion = new IngredientPortion();
        newIngredientPortion.setWeight(ingredientPortion.getWeight());
        newIngredientPortion.setIngredient((Ingredient) ingredientsDAO.show(ingredientPortion.getIngredientId()));
        newIngredientPortion.setDish((Dish) dishesDAO.show(id));

        dishesDAO.addIngredient(id, newIngredientPortion);
        return modelAndView;
    }

    @DeleteMapping("/{did}/{iid}")
    public ModelAndView deleteIngredient(@PathVariable("did") int did, @PathVariable("iid") int iid) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/" + did + "/update");
        Dish dish = (Dish) dishesDAO.show(did);
        dish.deleteIngredientPortionByIngredientId(iid);
        dishesDAO.update(did, dish);
        return modelAndView;
    }

    @PatchMapping("/{id}")
    public ModelAndView updateName(@ModelAttribute("dish") @Valid Dish dish, BindingResult bindingResult,
                                   @ModelAttribute("ingredientPortion") IngredientPortion ingredientPortion,
                                   @PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/" + id + "/update");
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("dishes/update");
            Dish tmp = (Dish) dishesDAO.show(id);
            dish.setIngredients(tmp.getIngredients());
            dish.setCookedWeight(tmp.getCookedWeight());
            modelAndView.addObject("dish", dish);
            modelAndView.addObject("ingredients", ingredientsDAO.index());
            return modelAndView;
        }
        dishesDAO.updateName(id, dish);
        return modelAndView;
    }

    @PostMapping()
    public ModelAndView create(@ModelAttribute("dish") @Valid Dish dish, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("dishes/new");
            return modelAndView;
        }
        for (IngredientPortion ingredientPortion : dish.getIngredients()) {
            ingredientPortion.setDish(dish);
            ingredientPortion.setIngredient((Ingredient) ingredientsDAO.show(ingredientPortion.getIngredientId()));
        }
        dishesDAO.save(dish);
        modelAndView.setViewName("redirect:/dishes/" + dish.getId() + "/update");
        return modelAndView;
    }

    @PatchMapping("/{id}/ingredient")
    public ModelAndView updateIngredient(@ModelAttribute("ingredientPortion") @Valid IngredientPortion ingredientPortion,
                                         BindingResult bindingResult ,@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/{id}/update");
        Dish dish = (Dish) dishesDAO.show(id);
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("dishes/update");
            modelAndView.addObject("dish", dish);
            modelAndView.addObject("ingredients", ingredientsDAO.index());
            return modelAndView;
        }
        dish.updateIngredientWeight(ingredientPortion);
        dishesDAO.update(id, dish);
        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes");
        dishesDAO.delete(id);
        return modelAndView;
    }

    @PatchMapping("/{id}/weight")
    public ModelAndView updateWeight(@ModelAttribute("dish") @Valid Dish dish, BindingResult bindingResult,
                                     @ModelAttribute("ingredientPortion") IngredientPortion ingredientPortion,
                                     @PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/{id}/update");
        Dish thisDish = (Dish) dishesDAO.show(id);
        if (bindingResult.hasErrors() || dish.getCookedWeight() > thisDish.getRawWeight()) {
            if(dish.getCookedWeight() > thisDish.getRawWeight()) {
                final String MSG = "Вес готового блюда не может быть больше веса ингредиентов";
                bindingResult.rejectValue("cookedWeight", "error.dish", MSG);
            }
            modelAndView.setViewName("dishes/update");
            dish.setIngredients(thisDish.getIngredients());
            modelAndView.addObject("dish", dish);
            modelAndView.addObject("ingredients", ingredientsDAO.index());
            return modelAndView;
        }
        thisDish.setCookedWeight(dish.getCookedWeight());
        dishesDAO.update(id, thisDish);
        return modelAndView;
    }


}
