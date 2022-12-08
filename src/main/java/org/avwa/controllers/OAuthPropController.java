package org.avwa.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.avwa.entities.OAuthProperties;
import org.avwa.enums.OAuthPropertiesNamesEnum;
import org.avwa.system.ApplicationEJB;
import org.avwa.system.authentication.oAuth.OAuthProviderType;
import org.avwa.utils.AnnotationsUtils;
import org.slf4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

@Named("oauthPropertiesController")
@ViewScoped
public class OAuthPropController extends BaseController<OAuthProperties> {

    @Inject
    Logger log;

    @Inject
    ApplicationEJB applicationEJB;

    private Map<String, Object> prop;

    private final String providerParameterName = "provider";

    private OAuthProviderType providerEnum = null;

    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
        String parameterProvider = request.getParameter(providerParameterName);

        if (parameterProvider != null)
            try {
                providerEnum = OAuthProviderType.valueOf(parameterProvider.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage(), e);
            }

        if (providerEnum != null)
            readPropertiesFromDB(providerEnum);
    }

    private void readPropertiesFromDB(OAuthProviderType providerType) {
        prop = new HashMap<>();

        String jpqlQuery = "SELECT p FROM " + AnnotationsUtils.getEntityName(OAuthProperties.class)
                + " p WHERE p.provider = :provider";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("provider", providerType);

        List<OAuthProperties> propDB = entService.findList(jpqlQuery, parameters);

        for (int i = 0; i < propDB.size(); i++) {
            OAuthProperties propertyDB = propDB.get(i);
            String key = propertyDB.getName().name();
            prop.put(key, propertyDB.getValue());
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveProperties() {
        for (Map.Entry<String, Object> entry : prop.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            String jpqlQuery = "UPDATE " + AnnotationsUtils.getEntityName(OAuthProperties.class)
                    + " AS p SET p.value = :value WHERE p.name = :name AND p.provider = :provider";

            Map<String, Object> parameters = new HashMap<>();

            parameters.put("name", OAuthPropertiesNamesEnum.valueOf(name));
            parameters.put("provider", providerEnum);
            parameters.put("value", String.valueOf(value));

            entService.executeUpdate(jpqlQuery, parameters);
        }
    }

    public String getProviderProperCase() {
        if (providerEnum != null)
            return StringUtils.capitalize(providerEnum.name().toLowerCase());
        return "";
    }

    public boolean controllerReady() {
        if (providerEnum != null)
            return true;
        else
            return false;
    }

    public Map<String, Object> getProp() {
        return prop;
    }

    public void setProp(Map<String, Object> prop) {
        this.prop = prop;
    }

}
