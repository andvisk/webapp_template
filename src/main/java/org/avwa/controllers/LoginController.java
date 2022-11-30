package org.avwa.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.avwa.entities.User;
import org.avwa.enums.UserTypeEnum;
import org.avwa.system.JsfUtilsEJB;
import org.avwa.system.SessionEJB;
import org.avwa.utils.AnnotationsUtils;
import org.avwa.utils.Pbkdf2;
import org.slf4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("loginController")
@ViewScoped
public class LoginController extends BaseController<User> {

    @Inject
    Logger log;

    @Inject
    SessionEJB sessionEJB;

    @Inject
    JsfUtilsEJB jsfUtilsEJB;

    private String email;

    private String passwordString;

    private String message;

    @PostConstruct
    public void init() {
        message = "";
    }

    public void authenticate() {

        String query = "SELECT e FROM " + AnnotationsUtils.getEntityName(User.class)
                + " e WHERE e.email = :email AND e.type = " + UserTypeEnum.class.getCanonicalName() + ".LOCAL";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", email);
        User userFromDb = (User) entService.find(query, parameters);

        if (userFromDb != null) {
            byte[] passw = Pbkdf2.getHash(passwordString, userFromDb.getSalt());
            if (Arrays.equals(passw, userFromDb.getPasswordHash())) {

                log.debug("authentication: passed");
                sessionEJB.setUser(userFromDb);

                jsfUtilsEJB.redirectTo(""); //landing page
            }
        }

        message = "Nepavyko prisijungti";
        sessionEJB.setUser(null);
        log.debug("authentication: failed");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordString() {
        return passwordString;
    }

    public void setPasswordString(String passwordString) {
        this.passwordString = passwordString;
    }

}
