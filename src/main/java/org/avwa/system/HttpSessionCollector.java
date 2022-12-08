package org.avwa.system;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class HttpSessionCollector implements HttpSessionListener {

    @Inject
    ApplicationEJB applicationEJB;

    @Inject
    TodoJobManager todoJobManager;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();

        HttpSessionInfo info = new HttpSessionInfo(session.getId(), "", session.getCreationTime());
        applicationEJB.getHttpsessions().put(session.getId(), info);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        applicationEJB.getHttpsessions().remove(event.getSession().getId());
    }

}
