package org.avwa.system;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

    private String oAuthState = "noState";

    private Map<String, String> messages = new HashMap<>();

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
            if (StringUtils.isNoneBlank(user.getName()))
                return user.getName();
            else {
                return user.getEmail();
            }
        }
        return "user_not_logged_in";
    }

    public String getoAuthState() {
        return oAuthState;
    }

    public void setoAuthState(String oAuthState) {
        this.oAuthState = oAuthState;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

}
