package ru.viol.caloriescalculatorboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootApplication
public class CaloriesCalculatorBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaloriesCalculatorBootApplication.class, args);
    }

}
