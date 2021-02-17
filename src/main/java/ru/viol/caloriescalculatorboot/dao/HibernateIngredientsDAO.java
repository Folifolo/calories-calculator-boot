package ru.viol.caloriescalculatorboot.dao;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.viol.caloriescalculatorboot.config.HibernateUtil;
import ru.viol.caloriescalculatorboot.models.Ingredient;

import java.util.List;

@Component
@Qualifier("hibernateIngredient")
public class HibernateIngredientsDAO implements IngredientsDAO{
    @Override
    public Object index() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<Ingredient> list = session.createQuery("from Ingredient ").list();
        session.getTransaction().commit();
        session.close();

        return list;
    }

    @Override
    public Object show(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Ingredient ingredient = session.get(Ingredient.class, id);
        session.getTransaction().commit();
        session.close();

        return ingredient;
    }

    @Override
    public void save(Ingredient ingredient) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(ingredient);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        session.delete(ingredient);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(int id, Ingredient ingredient) {
        ingredient.setId(id);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        ingredient.setId(id);
        session.update(ingredient);
        session.getTransaction().commit();
        session.close();
    }



}