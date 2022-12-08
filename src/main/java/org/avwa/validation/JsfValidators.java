package org.avwa.validation;

import java.util.regex.Pattern;

import org.slf4j.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Named
@ApplicationScoped
public class JsfValidators {

    @Inject
    Logger log;

    @PersistenceContext
    private EntityManager em;

    public static final String emailRegexPattern = "^(.+)@(\\S+)\\.(\\S+)$";

    public void notNull(FacesContext facesContext,
            UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            FacesMessage msg = new FacesMessage("", "Pasirinkite");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

    public void email(FacesContext facesContext,
            UIComponent component, Object value) throws ValidatorException {
        String testValue = value.toString();

        Pattern ptr = Pattern.compile(emailRegexPattern);

        if (!ptr.matcher(testValue).matches()) {
            FacesMessage msg = new FacesMessage("", "Patikrinkite el.p. adresą");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

    public void stringNotEmpty(FacesContext facesContext,
            UIComponent component, Object value) throws ValidatorException {
        if (value == null || value.toString().length() < 1) {
            FacesMessage msg = new FacesMessage("", "Įveskite");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
