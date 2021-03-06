package ru.viol.caloriescalculatorboot.models;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity
@Table(name = "dish_portion")
public class DishPortion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    private Date date;

    @Min(value = 0, message = "Количество калорий не может быть отрицательным")
    private double calories;

    @Transient
    private int cookedWeight;

    @Transient
    private double dishCalories;

    public void calculateCaloriesOnWeight() {
        if (dishCalories == 0)
            setDishCalories();
        calories = dishCalories * (cookedWeight / (double) dish.getCookedWeight());
    }

    public String printDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd-MM-yyyy");
        return sdf.format(date);
    }

    public double getDishCalories() {
        return dishCalories;
    }

    public void setDishCalories(double dishCalories) {
        this.dishCalories = dishCalories;
    }

    public void setDishCalories() {
        this.dishCalories = dish.getCalories();
    }

    public int getCookedWeight() {
        return cookedWeight;
    }

    public void setCookedWeight(int cookedWeight) {
        this.cookedWeight = cookedWeight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Dish getDish() {
        return dish;
    }

    public int getDishId() {
        return dish.getId();
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setCurrentDate() {
        date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
    }
}
