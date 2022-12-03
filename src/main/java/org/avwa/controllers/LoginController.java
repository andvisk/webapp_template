package org.avwa.controllers;

import java.applet.AppletStub;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.avwa.entities.ChangePasswdTokens;
import org.avwa.entities.User;
import org.avwa.enums.AppPropNamesEnum;
import org.avwa.enums.UserTypeEnum;
import org.avwa.freemarker.Freemarker;
import org.avwa.system.ApplicationEJB;
import org.avwa.system.JsfUtilsEJB;
import org.avwa.system.SessionEJB;
import org.avwa.utils.AnnotationsUtils;
import org.avwa.utils.MailUtils;
import org.avwa.utils.Pbkdf2;
import org.avwa.utils.StringUtils;
import org.slf4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.mail.Session;

@Named("loginController")
@ViewScoped
public class LoginController extends BaseController<User> {

    @Inject
    Logger log;

    @Inject
    SessionEJB sessionEJB;

    @Inject
    JsfUtilsEJB jsfUtilsEJB;

    @Resource(mappedName = "java:jboss/mail/MailSession")
    private Session mailSession;

    @Inject
    ApplicationEJB applicationEJB;

    private String email;

    private String passwordString;

    private String errorMsgKey = "error_msg";

    @PostConstruct
    public void init() {

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

                jsfUtilsEJB.redirectTo(""); // landing page

                return;
            }
        }

        sessionEJB.getMessages().put(errorMsgKey, "Nepavyko prisijungti");

        sessionEJB.setUser(null);
        log.debug("authentication: failed");
    }

    public void userForgotPassword() {

        String randomString = StringUtils.getRandomString(70);

        ChangePasswdTokens tokenEnt = new ChangePasswdTokens();
        tokenEnt.setDateTimeIssued(LocalDateTime.now());
        tokenEnt.setToken(randomString);
        tokenEnt.setEmail(email);

        entService.merge(tokenEnt);

        String url = applicationEJB.getProperty(AppPropNamesEnum.DOMAIN_FULL_URL) + applicationEJB.getContextPath() + "/forgotPassword?" + randomString;
        Map<String, Object> data = new HashMap<>(1);
        data.put("url", url);

        String emailContent = Freemarker.process(applicationEJB, data, "forgotPassword.ftl");

        MailUtils.sendEmail(mailSession, email, "Slaptažodžio keitimas", emailContent,
                applicationEJB.getProperty(AppPropNamesEnum.SENDING_MAIL_FROM_ADDRESS.name()));

        // reset email value in form
        email = "";
    }

    public void changePassword(){

        String query = "SELECT e FROM " + AnnotationsUtils.getEntityName(User.class)
                + " e, "+AnnotationsUtils.getEntityName(ChangePasswdTokens.class)+" t WHERE e.email = :email AND e.email = t.email AND t.token = :token AND e.type = " + UserTypeEnum.class.getCanonicalName() + ".LOCAL";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", email);
        User userFromDb = (User) entService.find(query, parameters);
        asdf
    }

    public String getMessage() {
        // use just one time, and clear
        String msg = sessionEJB.getMessages().get(errorMsgKey);
        sessionEJB.getMessages().put(errorMsgKey, "");
        return msg;
    }

    public void setMessage(String message) {
        sessionEJB.getMessages().put(errorMsgKey, message);
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
