package org.avwa.validation;

import java.util.List;
import java.util.regex.Pattern;

import org.avwa.controllers.UserProfileController;
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

    public void email_usersController(FacesContext facesContext,
            UIComponent component, Object value) throws ValidatorException {

        UsersController usersController = facesContext.getApplication().evaluateExpressionGet(facesContext,
                "#{usersController}", UsersController.class);
        User user = usersController.getObject();

        email(facesContext, component, value, user);

    }

    public void email_userProfileController(FacesContext facesContext,
            UIComponent component, Object value) throws ValidatorException {

        UserProfileController userProfileController = facesContext.getApplication().evaluateExpressionGet(facesContext,
                "#{userProfileController}", UserProfileController.class);
        User user = userProfileController.getObject();

        email(facesContext, component, value, user);
    }

    public <T> void email(FacesContext facesContext,
            UIComponent component, Object value, User user)
            throws ValidatorException {
        String testValue = value.toString();

        Pattern ptr = Pattern.compile(JsfValidators.emailRegexPattern);

        String reason = "";

        if (!ptr.matcher(testValue).matches()) {
            reason = "Patikrinkite el.p. adresą";
        } else {
            if (testValue.length() >= 4) {

                if (user.getId() == null) { // creating new user
                    TypedQuery<User> query = em.createQuery(
                            "SELECT e FROM " + AnnotationsUtils.getEntityName(User.class)
                                    + " e WHERE e.email = :email AND e.type = " + UserTypeEnum.class.getCanonicalName()
                                    + ".LOCAL",
                            User.class);
                    List<User> list = query.setParameter("email", testValue).getResultList();
                    if (list.size() > 0) {
                        reason = "Tokiu el.paštu vartotojas jau yra";
                    }
                } else { // changing existing user
                    TypedQuery<User> query = em.createQuery(
                            "SELECT e FROM " + AnnotationsUtils.getEntityName(User.class)
                                    + " e WHERE e.email = :email AND e.type = " + UserTypeEnum.class.getCanonicalName()
                                    + ".LOCAL AND e.id <> :id",
                            User.class);
                    query.setParameter("email", testValue);
                    List<User> list = query
                            .setParameter("id", user.getId())
                            .getResultList();
                    if (list.size() > 0) {
                        reason = "Tokiu el.paštu vartotojas jau yra";
                    }
                }
            } else {
                reason = "Min 4 simboliai";
            }
        }
        if (reason.length() > 0) {
            FacesMessage msg = new FacesMessage("", reason);
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

    public void emailExisting(FacesContext facesContext,
            UIComponent component, Object value) throws ValidatorException {
        String testValue = value.toString();

        Pattern ptr = Pattern.compile(JsfValidators.emailRegexPattern);

        String reason = "Patikrinkite el.p. adresą";

        boolean passed = true;

        if (!ptr.matcher(testValue).matches()) {
            passed = false;
        } else {
            if (testValue.length() >= 4) {
                UsersController usersController = facesContext.getApplication().evaluateExpressionGet(facesContext,
                        "#{usersController}", UsersController.class);
                if (usersController.getObject().getId() == null) {
                    TypedQuery<User> query = em.createQuery(
                            "SELECT e FROM " + AnnotationsUtils.getEntityName(User.class)
                                    + " e WHERE e.email = :email AND e.type = " + UserTypeEnum.class.getCanonicalName()
                                    + ".LOCAL",
                            User.class);
                    List<User> list = query.setParameter("email", testValue).getResultList();
                    if (list.size() == 0) {
                        passed = false;
                    }
                }
            } else {
                passed = false;
            }
        }
        if (!passed) {
            FacesMessage msg = new FacesMessage("", reason);
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
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
