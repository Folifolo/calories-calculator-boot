package ru.viol.caloriescalculatorboot.models;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "dish_ingredient")
public class IngredientPortion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(name = "weight")
    int weight;

    public IngredientPortion(Ingredient ingredient, int weight) {
        this.ingredient = ingredient;
        this.weight = weight;
    }

    public IngredientPortion() {
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getCalories() {
        return ingredient.getCalories() * weight * 0.01;
    }

    public int getIngredientId() {
        return ingredient.getId();
    }

    public String getName() {
        return ingredient.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientPortion that = (IngredientPortion) o;
        return weight == that.weight && ingredient.equals(that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, weight);
    }

    @Override
    public String toString() {
        return "IngredientPortion{" +
                "ingredient=" + ingredient +
                ", weight=" + weight +
                '}';
    }
}
