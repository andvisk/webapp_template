package org.avwa.controllers;

import org.avwa.entities.User;
import org.avwa.system.JsfUtilsEJB;
import org.avwa.system.SessionEJB;
import org.avwa.utils.Pbkdf2;
import org.slf4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("userProfileController")
@ViewScoped
public class UserProfileController extends BaseController<User> {

    @Inject
    Logger log;

    @Inject
    SessionEJB sessionEJB;

    @Inject
    JsfUtilsEJB jsfUtilsEJB;

    private User object;
    private boolean creatingNewObject = true;

    private String passwordString;

    @PostConstruct
    public void init() {

        object = sessionEJB.getUser();

    }

    public void saveObject() {
        if (creatingNewObject) {
            byte[] salt = Pbkdf2.getSalt();
            byte[] passw = Pbkdf2.getHash(passwordString, salt);
            object.setPasswordHash(passw);
            object.setSalt(salt);
        }
        entService.merge(object);
        sessionEJB.setUser(object);
    }

    public User getObject() {
        return object;
    }

    public void setObject(User object) {
        this.object = object;
    }

    public String getPasswordString() {
        return passwordString;
    }

    public void setPasswordString(String passwordString) {
        this.passwordString = passwordString;
    }

}
