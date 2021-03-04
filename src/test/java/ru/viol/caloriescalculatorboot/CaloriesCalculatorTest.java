package ru.viol.caloriescalculatorboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CaloriesCalculatorTest {

    public static void main(String[] args) {
        SpringApplication.run(CaloriesCalculatorTest.class, args);
    }

}

