package org.avwa.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.avwa.entities.ApplicationProperties;
import org.avwa.jpaUtils.EntitiesService;
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
    Logger log;

    @Inject
    EntitiesService entitiesService;

    private String contextPath;

    private List<ApplicationProperties> properties = new ArrayList<>();

    @PostConstruct
    public void init() {
        readProperties();
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        log.debug("setting context path - " + contextPath);
        this.contextPath = contextPath;
    }

    public void readProperties() {
        properties = entitiesService.findAll(ApplicationProperties.class);
    }

    public boolean getPropertyAsBoolean(String name) {
        return getPropertyAsBoolean(name, "");
    }

    public boolean getPropertyAsBoolean(String name, String parameter) {
        return org.avwa.utils.StringUtils.getAsBoolean(getProperty(name, parameter));
    }

    public String getProperty(String name) {
        return getProperty(name, "");
    }

    public String getProperty(String name, String parameter) {
        ApplicationProperties prop = properties.stream().filter(
                p -> StringUtils.equals(p.getName().name(), name) &&
                        (p.getParameter() == null && (parameter == null || parameter.length() == 0) ||
                                StringUtils.equals(p.getParameter(), parameter)))
                .findAny().orElse(null);
        return prop.getValue();
    }
}
