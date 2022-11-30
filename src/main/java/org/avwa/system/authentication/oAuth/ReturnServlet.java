package org.avwa.system.authentication.oAuth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.avwa.entities.User;
import org.avwa.enums.UserRoleEnum;
import org.avwa.enums.UserTypeEnum;
import org.avwa.jpaUtils.EntitiesService;
import org.avwa.utils.AnnotationsUtils;
import org.slf4j.Logger;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/socialauth")
public class ReturnServlet extends HttpServlet {

    @Inject
    Logger log;

    @Inject
    OAuth oAuth;

    @Inject
    EntitiesService entService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String provider = request.getParameter("provider").toUpperCase();
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        if (code != null) {
            OAuthProviderType providerType = OAuthProviderType.valueOf(provider);

            // get id, name and email
            Map<String, String> resources = oAuth.getResources(code, state, providerType);

            if (resources != null) {

                String social_id = resources.get("id");
                String social_name = resources.get("name");
                String social_email = resources.get("email");

                // check if not new user
                String query = "SELECT e FROM " + AnnotationsUtils.getEntityName(User.class)
                        + " e WHERE e.social_id = :social_id AND e.type = " + UserTypeEnum.class.getCanonicalName()
                        + ".SOCIAL AND e.social_type = :social_type";

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("social_id", social_id);
                parameters.put("social_type", providerType);

                User userFromDb = (User) entService.find(query, parameters);

                if (userFromDb == null) { // create new user

                    userFromDb = new User();
                    userFromDb.setRole(UserRoleEnum.PUBLIC);
                    if (StringUtils.isNoneBlank(social_name))
                        userFromDb.setName(social_name);
                    if (StringUtils.isNoneBlank(social_email))
                        userFromDb.setEmail(social_email);
                    userFromDb.setSocial_id(social_id);
                    userFromDb.setSocial_type(providerType);
                    userFromDb.setType(UserTypeEnum.SOCIAL);

                    entService.merge(userFromDb);

                    log.debug("social authentication: new user");

                    // message = "Prisijungta";
                    // sessionEJB.setUser(userFromDb);
                } else {
                    log.debug("oAuth user already in db");
                }

            } else {
                log.warn("failed oAuth login");
            }
        }
    }
}
