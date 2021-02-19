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

    @PostMapping("/{id}/ingredient")
    public ModelAndView addIngredient(@ModelAttribute("ingredientPortion") IngredientPortion ingredientPortion,
                                      @PathVariable("id") int id) {
        IngredientPortion tmp = new IngredientPortion();
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/" + id + "/update");
        Dish dish = (Dish) dishesDAO.show(id);
        tmp.setWeight(ingredientPortion.getWeight());
        tmp.setIngredient((Ingredient) ingredientsDAO.show(ingredientPortion.getIngredientId()));
        tmp.setDish(dish);
        dishesDAO.addIngredient(dish.getId(), tmp);
        return modelAndView;
    }

    @DeleteMapping("/{did}/{iid}")
    public ModelAndView deleteIngredient(@PathVariable("did") int did, @PathVariable("iid") int iid) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/" + did + "/update");
        Dish dish = (Dish) dishesDAO.show(did);
        dish.deleteIngredient(iid);
        dishesDAO.update(did, dish);
        return modelAndView;
    }

    @PatchMapping("/{id}")
    public ModelAndView updateName(@ModelAttribute("dish") Dish dish, @PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/" + id + "/update");

        dishesDAO.updateName(id, dish);
        return modelAndView;
    }

    @PostMapping()
    public ModelAndView create(@ModelAttribute("dish") Dish dish) {
        ModelAndView modelAndView = new ModelAndView();
        for (IngredientPortion ingredientPortion : dish.getIngredients()) {
            ingredientPortion.setDish(dish);
            ingredientPortion.setIngredient((Ingredient) ingredientsDAO.show(ingredientPortion.getIngredientId()));
        }
        dishesDAO.save(dish);
        modelAndView.setViewName("redirect:/dishes/"+dish.getId()+"/update");
        return modelAndView;
    }
    @PatchMapping("/{id}/ingredient")
    public ModelAndView updateIngredient(@ModelAttribute("ingredientP") IngredientPortion ingredientPortion,
                                         @PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/{id}/update");
        System.out.println(ingredientPortion);
        Dish dish = (Dish) dishesDAO.show(id);
        System.out.println(123);
        dish.updateIngredientWeight(ingredientPortion);
        dishesDAO.update(id, dish);
        System.out.println(456);
        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes");
        dishesDAO.delete(id);
        return modelAndView;
    }

    @PatchMapping("/{id}/weight")
    public ModelAndView updatWeight(@PathVariable("id") int id,
                                    @ModelAttribute("dish") Dish dish) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dishes/{id}/update");
        Dish thisDish = (Dish) dishesDAO.show(id);
        if(dish.getCookedWeight() <= thisDish.getRawWeight())
            thisDish.setCookedWeight(dish.getCookedWeight());
        dishesDAO.update(id, thisDish);
        return modelAndView;
    }


}
