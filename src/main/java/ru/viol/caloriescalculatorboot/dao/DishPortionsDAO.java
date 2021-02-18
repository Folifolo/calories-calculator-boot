package ru.viol.caloriescalculatorboot.dao;

import org.springframework.stereotype.Component;
import ru.viol.caloriescalculatorboot.models.DishPortion;

import java.sql.Date;


@Component
public interface DishPortionsDAO {

    public Object index();

    public Object show(int id);

    public void save(DishPortion dishPortion);


    public void delete(int id);

    public void update(int id, DishPortion dishPortion);

    Object indexByDate(Date date);

    Object indexDays();
}
