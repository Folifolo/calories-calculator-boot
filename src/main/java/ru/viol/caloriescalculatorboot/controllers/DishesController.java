package ru.viol.caloriescalculatorboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.viol.caloriescalculatorboot.dao.DishesDAO;
import ru.viol.caloriescalculatorboot.dao.IngredientsDAO;
import ru.viol.caloriescalculatorboot.models.Dish;
import ru.viol.caloriescalculatorboot.models.Ingredient;
import ru.viol.caloriescalculatorboot.models.IngredientPortion;

import javax.validation.Valid;

@Controller
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
    public String index(Model model) {
        model.addAttribute("dishes", dishesDAO.index());
        return "dishes/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id) {
        tmpDish = (Dish)dishesDAO.show(id);
        System.out.println(tmpDish);
        return "redirect:/dishes/temp";
    }

    @GetMapping("/temp")
    public String showTemp(Model model) {
        model.addAttribute("dish", tmpDish);
        return "dishes/show";
    }

    @PostMapping("/{id}/ingredient")
    public String addIngredient(@ModelAttribute("ingredientPortion") IngredientPortion ingredientPortion,
                                @PathVariable("id") int id) {
        Dish dish = (Dish)dishesDAO.show(id);
        ingredientPortion.setIngredient((Ingredient)ingredientsDAO.show(ingredientPortion.getIngredientId()));
        dish.addIngredient(ingredientPortion);
        System.out.println(ingredientPortion);
        dishesDAO.update(id, dish);
        return "redirect:/dishes/{id}/update";
    }

    @DeleteMapping("/{did}/{iid}")
    public String deleteIngredient(@PathVariable("did") int did, @PathVariable("iid") int iid) {
        System.out.println("pppp");
        Dish dish = (Dish)dishesDAO.show(did);
        dish.deleteIngredient(iid);
        dishesDAO.update(did, dish);
        return "redirect:/dishes/{did}/update";
    }

    @PatchMapping("/temp")
    public String updateTemp(@ModelAttribute("dish") Dish dish) {
        tmpDish.copyWeights(dish);
        return "redirect:/dishes/temp";
    }


    @PatchMapping("/{id}")
    public String updateName(@ModelAttribute("dish") @Valid Dish dish,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        if(bindingResult.hasErrors())
            return "dishes/update";
        dishesDAO.updateName(id, dish);
        return "redirect:/dishes/{id}/update";
    }

    @GetMapping("/create")
    public String eraseTemp() {
        tmpDish = new Dish();
        return "redirect:/dishes/new";
    }

    @GetMapping("/new")
    public String newDish(@ModelAttribute("ingredientPortion") IngredientPortion ingredientPortion, Model model) {
        model.addAttribute("dish", tmpDish);
        model.addAttribute("ingredients", ingredientsDAO.index());
        return "dishes/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("dish") Dish dish) {
        dishesDAO.save(dish);
        return "redirect:/dishes";
    }

    @PostMapping("/temp/ingredient")
    public String addTmpIngredient(@ModelAttribute("ingredientPortion") IngredientPortion ingredientPortion) {
        Ingredient ingredient = (Ingredient) ingredientsDAO.show(ingredientPortion.getIngredientId());
        ingredientPortion.setIngredient(ingredient);
        tmpDish.addIngredient(ingredientPortion);
        System.out.println(ingredientPortion);
        return "redirect:/dishes/new";
    }

    @PatchMapping("/{id}/ingredient")
    public String updateIngredient(@ModelAttribute("ingredientP") IngredientPortion ingredientPortion,
                                @PathVariable("id") int id) {
        System.out.println(ingredientPortion);
        Dish dish = (Dish)dishesDAO.show(id);
        System.out.println(123);
        dish.updateIngredientWeight(ingredientPortion);
        dishesDAO.update(id, dish);
        System.out.println(456);
        return "redirect:/dishes/{id}/update";
    }

    @DeleteMapping("/temp/{id}")
    public String deleteTmpIngredient(@PathVariable("id") int id) {
        tmpDish.deleteIngredient(id);
        return "redirect:/dishes/new";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        dishesDAO.delete(id);
        return "redirect:/dishes";
    }

    @GetMapping("/{id}/update")
    public String update(@ModelAttribute("ingredientPortion") IngredientPortion ingredientPortion,
                         @PathVariable("id") int id, Model model) {
        model.addAttribute("dish", dishesDAO.show(id));
        model.addAttribute("ingredients", ingredientsDAO.index());
        return "dishes/update";
    }

}
