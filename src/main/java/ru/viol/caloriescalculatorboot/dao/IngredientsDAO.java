package ru.viol.caloriescalculatorboot.dao;

import org.springframework.stereotype.Component;
import ru.viol.caloriescalculatorboot.models.Ingredient;


@Component
public interface IngredientsDAO {

    public Object index();

    public Object show(int id);

    public void save(Ingredient ingredient);


    public void delete(int id);

    public void update(int id, Ingredient ingredient);
}
