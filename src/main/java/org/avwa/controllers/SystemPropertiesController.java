package org.avwa.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.avwa.entities.ApplicationProperties;
import org.avwa.entities.User;
import org.avwa.enums.AppPropNamesEnum;
import org.avwa.system.ApplicationEJB;
import org.avwa.utils.AnnotationsUtils;
import org.slf4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("systemPropertiesController")
@ViewScoped
public class SystemPropertiesController extends BaseController<User> {

    @Inject
    Logger log;

    @Inject
    ApplicationEJB applicationEJB;

    private Map<String, Object> prop;

    private final String nameParameterSplitter = "___";

    @PostConstruct
    public void init() {
        readProperties();
    }

    private void readProperties() {
        List<ApplicationProperties> propApp = applicationEJB.getProperties();
        prop = new HashMap<>();
        for (int i = 0; i < propApp.size(); i++) {
            ApplicationProperties propertyApp = propApp.get(i);
            String key = propertyApp.getName()
                    + ((StringUtils.isNoneBlank(propertyApp.getParameter()))
                            ? nameParameterSplitter + propertyApp.getParameter()
                            : "");
            prop.put(key, propertyApp.getValue());
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveProperties() {
        for (Map.Entry<String, Object> entry : prop.entrySet()) {
            String key = entry.getKey();
            String name = key;
            String parameter = null;
            Object value = entry.getValue();
            if (key.indexOf(nameParameterSplitter) > 0) {
                String[] nameParameter = key.split(nameParameterSplitter);
                name = nameParameter[0];
                parameter = nameParameter[1];
            }
            String jpqlQuery = "UPDATE " + AnnotationsUtils.getEntityName(ApplicationProperties.class)
                    + " AS p SET p.value = :value WHERE p.name = :name";

            Map<String, Object> parameters = new HashMap<>();

            if (parameter != null) {
                jpqlQuery += " AND p.parameter = :parameter";
                parameters.put("parameter", parameter);
            }

            parameters.put("name", AppPropNamesEnum.valueOf(name));

            parameters.put("value", String.valueOf(value));

            entService.executeUpdate(jpqlQuery, parameters);
        }

        applicationEJB.readProperties();
    }

    public Map<String, Object> getProp() {
        return prop;
    }

    public void setProp(Map<String, Object> prop) {
        this.prop = prop;
    }

}
