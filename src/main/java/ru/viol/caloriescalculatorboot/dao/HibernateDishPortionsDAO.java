package ru.viol.caloriescalculatorboot.dao;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.viol.caloriescalculatorboot.config.HibernateUtil;
import ru.viol.caloriescalculatorboot.models.DishPortion;

import java.sql.Date;
import java.util.List;


@Component
@Qualifier("hibernateDishPortion")
public class HibernateDishPortionsDAO implements DishPortionsDAO {

    @Override
    public Object index() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<DishPortion> list = session.createQuery("from DishPortion  order by date desc").list();
        session.getTransaction().commit();
        session.close();

        return list;
    }

    @Override
    public Object show(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        DishPortion dishPortion = session.get(DishPortion.class, id);
        session.getTransaction().commit();
        session.close();

        return dishPortion;
    }

    @Override
    public void save(DishPortion dishPortion) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(dishPortion);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        DishPortion dishPortion = new DishPortion();
        dishPortion.setId(id);
        session.delete(dishPortion);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(int id, DishPortion dishPortion) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        dishPortion.setId(id);
        session.update(dishPortion);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Object indexByDate(Date date) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<DishPortion> list = session.createQuery("from DishPortion where date='"+date+ "'").list();
        session.getTransaction().commit();
        session.close();

        return list;
    }

    @Override
    public Object indexDays() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<Integer> list = session.createQuery("select distinct date from DishPortion order by date desc ").list();
        session.getTransaction().commit();
        session.close();

        return list;
    }
}
