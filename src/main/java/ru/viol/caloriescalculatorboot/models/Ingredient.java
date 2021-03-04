package ru.viol.caloriescalculatorboot.models;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "ingredient")
public class Ingredient implements Serializable {
    static final int INGREDIENT_WEIGHT = 100;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotEmpty(message = "Название не должно быть пустым")
    private String name;
    @Min(value = 0, message = "Количество калорий не может быть отрицательным")
    private int calories;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ingredient")
    List<IngredientPortion> ingredientPortions;

    public Ingredient() {
    }

    public Ingredient(Dish dish) {
        name = dish.getName();
        calories = (int) (dish.getCalories()/dish.getCookedWeight()*INGREDIENT_WEIGHT);
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

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id && calories == that.calories && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, calories);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", calories=" + calories +
                '}';
    }
}
