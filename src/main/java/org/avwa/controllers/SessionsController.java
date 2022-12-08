package org.avwa.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.avwa.system.ApplicationEJB;
import org.avwa.system.SessionEJB;
import org.slf4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

@Named("sessionsController")
@ViewScoped
public class SessionsController extends BaseController<HttpSession> {

    @Inject
    Logger log;

    @Inject
    ApplicationEJB applicationEJB;

    @Inject
    SessionEJB sessionEJB;

    private final String dateAndTimeFormat = "yyyy MM dd HH:mm";

    private HttpSession object;
    private Class clazz = HttpSession.class;

    private List<Map<String, String>> sessionData;

    @PostConstruct
    public void init() {

    }

    public List<Map<String, String>> getSessionData() {
        sessionData = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat(dateAndTimeFormat);

        for (Map.Entry<String, HttpSession> sessionEntry : applicationEJB.getHttpsessions().entrySet()) {
            Map<String, String> data = new HashMap<>(3);
            data.put("id", sessionEntry.getValue().getId());
            data.put("created", format.format(sessionEntry.getValue().getCreationTime()));

            String userName = applicationEJB.getSessionsejb().get(sessionEntry.getKey()).getUserName();

            data.put("user", userName);
            sessionData.add(data);
        }

        return sessionData;

    }

}
