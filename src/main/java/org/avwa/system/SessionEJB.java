package org.avwa.system;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.avwa.entities.User;
import org.avwa.enums.UserRoleEnum;
import org.avwa.jpaUtils.EntitiesService;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("sessionEJB")
@SessionScoped
@Stateful
public class SessionEJB {

    

    @Inject
    EntitiesService entService;

    private User user;

    private String oAuthState = "noState";

    private final String publicNotRegisteredUserName = "Neprisijungęs";

    private Map<String, String> messages = new HashMap<>();

    @PostConstruct
    public void init() {
        setPublicUser();
    }

    public void setPublicUser() {
        user = new User();
        user.setName(publicNotRegisteredUserName);
        user.setRole(UserRoleEnum.PUBLIC);
    }

    public User getUser() {
        if (user == null)
            setPublicUser();
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void refreshUserFromDB() {
        if (user.getId() != null)
            user = entService.find(User.class, user.getId());
    }

    public String getUserName() {
        // ensure user != null (in case user is null -> creates public not registered
        // user)
        getUser();
        if (StringUtils.isNoneBlank(user.getName()))
            return user.getName();
        else if (StringUtils.isNoneBlank(user.getEmail())) {
            return user.getEmail();
        }
        return "Name, email - blank";
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
