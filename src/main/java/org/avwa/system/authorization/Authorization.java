package org.avwa.system.authorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.avwa.enums.UserRoleEnum;
import org.avwa.system.SessionEJB;
import org.avwa.utils.PathUtils;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("authz")
@Stateful
public class Authorization {

    @Inject
    SessionEJB sessionEJB;

    private Map<String, List<UserRoleEnum>> directoriesPerm;

    @PostConstruct
    public void init() {

        directoriesPerm = new HashMap<>(10);

        addDirPermission("admin", UserRoleEnum.ADMIN);
        addDirPermission("", Arrays.asList(UserRoleEnum.values()));
        addDirPermission("public", Arrays.asList(UserRoleEnum.values()));
        addDirPermission("socialauth", Arrays.asList(UserRoleEnum.values()));
    }

    private void addDirPermission(String dir, List<UserRoleEnum> roles) {
        for (UserRoleEnum role : roles) {
            addDirPermission(dir, role);
        }
    }

    private void addDirPermission(String dir, UserRoleEnum role) {
        dir = PathUtils.removeLeadingAndTrailingSlashes(dir);

        List<UserRoleEnum> roles = directoriesPerm.get(dir);
        if (roles == null)
            roles = new ArrayList<UserRoleEnum>();

        roles.add(role);

        directoriesPerm.put(dir, roles);
    }

    public boolean hasRole(ArrayList<String> roleArr) {
        for (String roleStr : roleArr) {
            UserRoleEnum role = UserRoleEnum.valueOf(roleStr.toUpperCase());
            if(sessionEJB.getUser().getRole().equals(role)){
                return true;
            }
        }
        return false;
    }

    public boolean hasNoRole(ArrayList<String> roleArr) {
        for (String roleStr : roleArr) {
            UserRoleEnum role = UserRoleEnum.valueOf(roleStr.toUpperCase());
            if(sessionEJB.getUser().getRole().equals(role)){
                return false;
            }
        }
        return true;
    }

    public boolean hasPermissionOnDir(String dir) {
        dir = PathUtils.removeLeadingAndTrailingSlashes(dir);

        List<UserRoleEnum> roles = directoriesPerm.get(dir);

        if (roles != null && sessionEJB.getUser() != null && roles.contains(sessionEJB.getUser().getRole())) {
            return true;
        }

        return false;
    }
}
