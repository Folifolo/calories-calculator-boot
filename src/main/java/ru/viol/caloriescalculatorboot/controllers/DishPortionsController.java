package ru.viol.caloriescalculatorboot.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.viol.caloriescalculatorboot.dao.DishPortionsDAO;
import ru.viol.caloriescalculatorboot.dao.DishesDAO;
import ru.viol.caloriescalculatorboot.models.Dish;
import ru.viol.caloriescalculatorboot.models.DishPortion;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/calendar")
public class DishPortionsController {
    DishPortionsDAO dishPortionsDAO;
    DishesDAO dishesDAO;

    @Autowired
    public DishPortionsController(@Qualifier("hibernateDishPortion") DishPortionsDAO dishPortionsDAO,
                                  @Qualifier("hibernateDish") DishesDAO dishesDAO) {
        this.dishPortionsDAO = dishPortionsDAO;
        this.dishesDAO = dishesDAO;
    }

    @GetMapping()
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("calendar/index");
        List<DishPortion> list = (List<DishPortion>) dishPortionsDAO.index();
        modelAndView.addObject("dishPortions", list);
        double caloriesSum = list.stream().mapToDouble(DishPortion::getCalories).sum();
        modelAndView.addObject("caloriesSum", caloriesSum);
        return modelAndView;
    }

    @GetMapping("/day/{date}")
    public ModelAndView indexByDay(@PathVariable("date") String strDate) {
        Date date = Date.valueOf(strDate);
        ModelAndView modelAndView = new ModelAndView("calendar/index");
        List<DishPortion> list = (List<DishPortion>) dishPortionsDAO.indexByDate(date);
        modelAndView.addObject("dishPortions", list);
        double caloriesSum = list.stream().mapToDouble(DishPortion::getCalories).sum();
        modelAndView.addObject("caloriesSum", caloriesSum);
        return modelAndView;
    }

    @GetMapping("/day")
    public ModelAndView indexDays() {
        ModelAndView modelAndView = new ModelAndView("calendar/day");
        modelAndView.addObject("dates", dishPortionsDAO.indexDays());
        return modelAndView;
    }

    @PostMapping()
    public ModelAndView create(@ModelAttribute("dish") DishPortion dishPortion) {
        ModelAndView modelAndView = new ModelAndView("redirect:/calendar");
        dishPortion.setDish((Dish) dishesDAO.show(dishPortion.getDishId()));
        dishPortion.setCurrentDate();
        dishPortionsDAO.save(dishPortion);
        return modelAndView;
    }
    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/calendar");
        dishPortionsDAO.delete(id);
        return modelAndView;
    }


}
