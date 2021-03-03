package ru.viol.caloriescalculatorboot.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dish")
public class Dish implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Название не должно быть пустым")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "dish", cascade = CascadeType.ALL)
    private List<IngredientPortion> ingredients;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dish", cascade = CascadeType.ALL)
    private List<DishPortion> dishPortions;

    @Column(name = "weight")
    @Min(value = 0, message = "Вес не может быть отрицательным")
    private int cookedWeight;

    public Dish() {
        this.ingredients = new ArrayList<>();
        this.dishPortions = new ArrayList<>();
    }

    public Dish(IngredientPortion ingredient) {
        this();
        name = ingredient.getName();
        ingredients.add(ingredient);
    }


    public double getCalories() {
        double calories = 0;
        for (IngredientPortion ingredient : ingredients) {
            calories += ingredient.getCalories();
        }
        return calories;
    }

    public int getRawWeight() {
        int weight = 0;
        for (IngredientPortion ingredient : ingredients) {
            weight += ingredient.getWeight();
        }
        return weight;
    }

    public IngredientPortion getIngredientPortionByIngredientId(int id) {
        for (IngredientPortion ingredient : ingredients) {
            if (ingredient.getIngredientId() == id) {
                return ingredient;
            }
        }
        return null;
    }

    public void addIngredient(IngredientPortion ingredientPortion) {
        for (IngredientPortion ingredient : ingredients) {
            if (ingredient.getIngredientId() == ingredientPortion.getIngredientId()) {
                ingredient.setWeight(ingredient.getWeight() + ingredientPortion.getWeight());
                return;
            }
        }
        ingredients.add(ingredientPortion);
    }

    public void deleteIngredientPortionByIngredientId(int id) {
        IngredientPortion tmp = null;
        for (IngredientPortion ingredient : ingredients) {
            if (ingredient.getIngredientId() == id) {
                tmp = ingredient;
            }
        }
        ingredients.remove(tmp);
    }

    public void copyWeights(Dish dish) {
        for (int i = 0; i < ingredients.size(); i++) {
            ingredients.get(i).setWeight(dish.getIngredients().get(i).getWeight());
        }
    }

    public void updateIngredientWeight(IngredientPortion ingredientPortion) {
        for (IngredientPortion ingredient : ingredients) {
            if (ingredient.getIngredientId() == ingredientPortion.getIngredientId()) {
                ingredient.setWeight(ingredientPortion.getWeight());
                return;
            }
        }
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(List<IngredientPortion> ingredients) {
        this.ingredients = ingredients;
    }

    public List<IngredientPortion> getIngredients() {
        return ingredients;
    }

    public List<DishPortion> getDishPortions() {
        return dishPortions;
    }

    public void setDishPortions(List<DishPortion> dishPortions) {
        this.dishPortions = dishPortions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        if (ingredients.size() != dish.ingredients.size())
            return false;

        for (int i = 0; i < ingredients.size(); i++) {
            if (!ingredients.get(i).equals(dish.ingredients.get(i)))
                return false;
        }
        return id == dish.id && name.equals(dish.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}
