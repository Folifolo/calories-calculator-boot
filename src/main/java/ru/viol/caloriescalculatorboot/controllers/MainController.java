package ru.viol.caloriescalculatorboot.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class MainController {

    @GetMapping()
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("main/main");
        return modelAndView;
    }
}
