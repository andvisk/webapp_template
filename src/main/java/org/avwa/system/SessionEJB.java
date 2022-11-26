package org.avwa.system;

import org.avwa.entities.User;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("sessionEJB")
@SessionScoped
@Stateful
public class SessionEJB {

    private User user;

    @PostConstruct
    public void init() {
        user = null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserName() {
        if (user != null) {
            return user.getName();
        }
        return "user_not_logged_in";
    }
}
