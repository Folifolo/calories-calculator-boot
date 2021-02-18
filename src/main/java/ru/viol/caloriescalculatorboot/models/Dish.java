package ru.viol.caloriescalculatorboot.models;

import javax.persistence.*;
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

    private String name;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "dish", cascade = CascadeType.ALL)
    private List<IngredientPortion> ingredients;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dish", cascade = CascadeType.ALL)
    private List<DishPortion> dishPortions;


    public double getCalories() {
        double calories = 0;
        for (IngredientPortion ingredient : ingredients) {
            calories += ingredient.getCalories();
        }
        return calories;
    }

    public Dish() {
        this.ingredients = new ArrayList<>();
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

    public IngredientPortion getIngredientByIngredientId(int id) {
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


    public void deleteIngredient(int id) {
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
