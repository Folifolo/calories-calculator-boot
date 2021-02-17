package ru.viol.caloriescalculatorboot.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.viol.caloriescalculatorboot.dao.IngredientsDAO;
import ru.viol.caloriescalculatorboot.models.Ingredient;

import javax.validation.Valid;

@Controller
@RequestMapping("/ingredients")
public class IngredientsController {

    private final IngredientsDAO ingredientsDAO;

    @Autowired
    public IngredientsController(@Qualifier("hibernateIngredient") IngredientsDAO ingredientsDAO) {
        this.ingredientsDAO = ingredientsDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("ingredients", ingredientsDAO.index());
        return "ingredients/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("ingredient", ingredientsDAO.show(id));
        return "ingredients/show";
    }

    @GetMapping("/new")
    public String newIngredient(@ModelAttribute("ingredient") Ingredient ingredient) {
        return "ingredients/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("ingredient") @Valid Ingredient ingredient,
                         BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "ingredients/new";
        ingredientsDAO.save(ingredient);
        return "redirect:/ingredients";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("ingredient") @Valid Ingredient ingredient,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        if(bindingResult.hasErrors())
            return "ingredients/{id}";
        ingredientsDAO.update(id, ingredient);
        return "redirect:/ingredients";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        ingredientsDAO.delete(id);
        return "redirect:/ingredients";
    }


}
