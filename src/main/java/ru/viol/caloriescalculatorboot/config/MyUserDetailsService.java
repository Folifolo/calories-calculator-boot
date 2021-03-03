package ru.viol.caloriescalculatorboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.viol.caloriescalculatorboot.dao.interfaces.UsersDAO;
import ru.viol.caloriescalculatorboot.models.User;
import ru.viol.caloriescalculatorboot.models.UserPrincipal;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersDAO usersDAO;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = (User) usersDAO.showByName(s);
        if (user == null)
            throw new UsernameNotFoundException("User 404");

        return new UserPrincipal(user);
    }
}
