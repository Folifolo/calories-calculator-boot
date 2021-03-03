package ru.viol.caloriescalculatorboot.dao;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.viol.caloriescalculatorboot.config.HibernateUtil;
import ru.viol.caloriescalculatorboot.dao.interfaces.DishesDAO;
import ru.viol.caloriescalculatorboot.models.Dish;
import ru.viol.caloriescalculatorboot.models.IngredientPortion;

import java.util.List;

@Component
@Qualifier("hibernateDish")
public class HibernateDishesDAO implements DishesDAO {

    @Override
    public Object index() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<Dish> list = session.createQuery("from Dish ").list();
        session.getTransaction().commit();
        session.close();

        return list;
    }

    @Override
    public Object show(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Dish dish = session.get(Dish.class, id);
        session.getTransaction().commit();
        session.close();

        return dish;
    }

    @Override
    public void update(int id, Dish dish) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(dish);
        session.getTransaction().commit();
        session.close();

    }

    @Override
    public void addIngredient(int id, IngredientPortion ingredientPortion) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Dish dish = session.load(Dish.class, id);
        dish.addIngredient(ingredientPortion);
        session.update(dish);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void save(Dish dish) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(dish);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Object showPortionByIngredientId(int did, int iid) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Dish dish = session.get(Dish.class, did);
        session.getTransaction().commit();
        session.close();

        return dish.getIngredientPortionByIngredientId(iid);
    }

    @Override
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Dish dish = session.load(Dish.class, id);

        for(IngredientPortion ingredientPortion : dish.getIngredients())
            session.delete(ingredientPortion);
        session.delete(dish);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void updateName(int id, Dish dish) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Dish tmp = session.load(Dish.class, id);
        tmp.setName(dish.getName());
        session.update(tmp);
        session.getTransaction().commit();
        session.close();
    }
}
