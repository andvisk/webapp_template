package org.avwa.validation;

import java.util.List;
import java.util.regex.Pattern;

import org.avwa.controllers.UsersController;
import org.avwa.entities.User;
import org.avwa.enums.UserTypeEnum;
import org.avwa.utils.AnnotationsUtils;
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
import jakarta.persistence.TypedQuery;

@Named
@ApplicationScoped
public class JsfValidatorsUser {

    @Inject
    Logger log;

    @Inject
    UsersController usersController;

    @PersistenceContext
    private EntityManager em;

    public void email(FacesContext facesContext,
            UIComponent component, Object value) throws ValidatorException {
        String testValue = value.toString();

        Pattern ptr = Pattern.compile("^(.+)@(\\S+)\\.(\\S+)$");

        if (!ptr.matcher(testValue).matches()) {
            FacesMessage msg = new FacesMessage("", "Patikrinkite el.p. adresą");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        } else {
            String reason = "";

            if (testValue.length() >= 4) {

                UsersController usersController = facesContext.getApplication().evaluateExpressionGet(facesContext,
                        "#{usersController}", UsersController.class);
                if (usersController.getObject().getId() == null) {
                    TypedQuery<User> query = em.createQuery(
                            "SELECT e FROM " + AnnotationsUtils.getEntityName(User.class)
                                    + " e WHERE e.email = :email AND e.type = " + UserTypeEnum.class.getCanonicalName() + ".LOCAL",
                            User.class);
                    List<User> list = query.setParameter("email", testValue).getResultList();
                    if (list.size() > 0) {
                        reason = "Tokiu el.paštu vartotojas jau yra";
                    }
                }
            } else {
                reason = "Min 4 simboliai";
            }
            if (reason.length() > 0) {
                FacesMessage msg = new FacesMessage("", reason);
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }

    }

    public void password(FacesContext facesContext,
            UIComponent component, Object value) throws ValidatorException {
        String testValue = value.toString();

        if (testValue.length() < 4) {
            FacesMessage msg = new FacesMessage("", "Min 4 simboliai");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
