package org.avwa.controllers;

import java.util.Map;

import org.avwa.entities.User;
import org.avwa.enums.UserRoleEnum;
import org.avwa.enums.UserTypeEnum;
import org.avwa.pfUtils.LazyDataModelExt;
import org.avwa.system.ApplicationEJB;
import org.avwa.system.JsfUtilsEJB;
import org.avwa.system.SessionEJB;
import org.avwa.system.authentication.oAuth.OAuthProviderType;
import org.avwa.utils.AnnotationsUtils;
import org.avwa.utils.Pbkdf2;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.slf4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("usersController")
@ViewScoped
public class UsersController extends BaseController<User> {

    @Inject
    Logger log;
    
    @Inject
    ApplicationEJB applicationEJB;

    @Inject
    SessionEJB sessionEJB;

    @Inject
    JsfUtilsEJB jsfUtilsEJB;

    private User object;
    private Class clazz = User.class;
    private String actionName = "";
    private boolean creatingNewObject = true;

    private String passwordString;

    @PostConstruct
    public void init() {

        object = createNewObject(clazz);

        setModel(
                new LazyDataModelExt<>(entService) {

                    String selectCount = "select count(a)";
                    String select = "select a";
                    String query = " from " + AnnotationsUtils.getEntityName(clazz) + " a";

                    @Override
                    public String getSubQueryFilterBy(Map<String, FilterMeta> filterBy) {
                        String filterQ = "";
                        for (FilterMeta filterMeta : filterBy.values()) {
                            if (filterQ.length() > 0)
                                filterQ += " and";
                            else
                                filterQ += " where";

                            if (filterMeta.getFilterValue().getClass().isEnum()) {
                                filterQ += " a." + filterMeta.getField() + " = :" + filterMeta.getField();
                            }
                            if (filterMeta.getFilterValue() instanceof String) {
                                filterQ += " lower(a." + filterMeta.getField() + ") like lower(concat('%', :"
                                        + filterMeta.getField() + ",'%'))";
                            }
                        }
                        return filterQ;
                    };

                    @Override
                    public String getSubQuerySortBy(Map<String, SortMeta> sortBy) {
                        String sortByQ = "";
                        for (SortMeta sortMeta : sortBy.values()) {
                            if (!sortMeta.getOrder().isUnsorted()) {
                                if (sortByQ.length() > 0)
                                    sortByQ += ",";
                                else
                                    sortByQ += " order by";
                                sortByQ += " a." + sortMeta.getField()
                                        + (sortMeta.getOrder().isDescending() ? " desc" : " asc");
                            }
                        }
                        return sortByQ;
                    };

                    @Override
                    public String getCountQuery(Map<String, FilterMeta> filterBy) {
                        String countQuery = selectCount + query + getSubQueryFilterBy(filterBy);
                        log.debug("Count query: " + countQuery);
                        return countQuery;
                    }

                    @Override
                    public String getLoadQuery(Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                        String loadQuert = select + query + getSubQueryFilterBy(filterBy) + getSubQuerySortBy(sortBy);
                        log.debug("Load query: " + loadQuert);
                        return loadQuert;
                    }
                });
    }

    public void saveObject() {
        if (creatingNewObject) {
            byte[] salt = Pbkdf2.getSalt();
            byte[] passw = Pbkdf2.getHash(passwordString, salt);
            object.setPasswordHash(passw);
            object.setSalt(salt);
        }
        entService.merge(object);
        object = createNewObject(clazz);
        passwordString = "";

        applicationEJB.refreshUsersInAllSessions();
    }

    public void userRegistration() {

        object.setType(UserTypeEnum.LOCAL);
        object.setRole(UserRoleEnum.PUBLIC_REGISTERED_USER);

        byte[] salt = Pbkdf2.getSalt();
        byte[] passw = Pbkdf2.getHash(passwordString, salt);
        object.setPasswordHash(passw);
        object.setSalt(salt);

        entService.merge(object);

        sessionEJB.setUser(object);

        object = createNewObject(clazz);
        passwordString = "";

       jsfUtilsEJB.redirectTo("/"); 
    }

    public void prepareForNewObject() {
        object = new User();
        object.setType(UserTypeEnum.LOCAL);
        creatingNewObject = true;
        actionName = "Naujas vartotojas";
    }

    public void prepareForEditingObject(User user) {
        object = entService.refresh(user); // dataTable on second selection sets null to rele, so refresh
        creatingNewObject = false;
        actionName = "Keisti vartotoją";
    }

    public User getObject() {
        return object;
    }

    public void setObject(User object) {
        this.object = object;
    }

    public boolean isCreatingNewObject() {
        return creatingNewObject;
    }

    public void setCreatingNewObject(boolean creatingNewObject) {
        this.creatingNewObject = creatingNewObject;
    }

    public String getActionName() {
        return actionName;
    }

    public String getPasswordString() {
        return passwordString;
    }

    public void setPasswordString(String passwordString) {
        this.passwordString = passwordString;
    }

    public UserRoleEnum[] getAllRoles() {
        return UserRoleEnum.values();
    }

    public OAuthProviderType[] getAllOAuthProviderTypes() {
        return OAuthProviderType.values();
    }
}
