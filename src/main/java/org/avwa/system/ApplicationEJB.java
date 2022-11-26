package org.avwa.system;

import org.slf4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ApplicationScoped
@Stateful
public class ApplicationEJB {

    @Inject
    Logger log;;

    private String contextPath;

    @PostConstruct
    public void init() {

    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        log.debug("setting context path - " + contextPath);
        this.contextPath = contextPath;
    }

}
