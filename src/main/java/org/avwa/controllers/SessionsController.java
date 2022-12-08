package org.avwa.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.avwa.system.ApplicationEJB;
import org.avwa.system.HttpSessionInfo;
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

    private HttpSession object;
    private Class clazz = HttpSession.class;

    private List<HttpSessionInfo> sessionData;

    @PostConstruct
    public void init() {

    }

    public List<HttpSessionInfo> getSessionData() {
        sessionData = new ArrayList<>();

        for (Map.Entry<String, HttpSessionInfo> sessionEntry : applicationEJB.getHttpsessions().entrySet()) {
            sessionData.add(sessionEntry.getValue());
        }

        return sessionData;

    }

}
