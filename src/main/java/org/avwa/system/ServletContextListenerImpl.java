package org.avwa.system;

import org.slf4j.Logger;

import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ServletContextListenerImpl implements ServletContextListener {

    @Inject
    ApplicationEJB applicationEJB;

    @Inject
    Logger log;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        applicationEJB.setContextPath(servletContextEvent.getServletContext().getContextPath());
        applicationEJB.configureFreeMarker(servletContextEvent.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
