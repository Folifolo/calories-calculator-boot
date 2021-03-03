package ru.viol.caloriescalculatorboot.dao;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.viol.caloriescalculatorboot.config.HibernateUtil;
import ru.viol.caloriescalculatorboot.dao.interfaces.UsersDAO;
import ru.viol.caloriescalculatorboot.models.User;


@Component
@Qualifier("hibernateUser")
public class HibernateUsersDAO implements UsersDAO {


    @Override
    public Object showByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        User user = (User) session.createQuery("from User where name= :name")
                .setParameter("name", name).uniqueResult();
        System.out.println(user.getName()+" " + user.getPassword());
        session.getTransaction().commit();
        session.close();
        return user;
    }
}
