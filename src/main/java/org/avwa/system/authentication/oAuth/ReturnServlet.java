package org.avwa.system.authentication.oAuth;

import java.io.IOException;
import java.util.Map;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String provider = request.getParameter("provider").toUpperCase();
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        if (code != null) {
            OAuthProviderType providerType = OAuthProviderType.valueOf(provider);

            Map<String, String> resources = oAuth.getResources(code, state, providerType);

            if (resources != null) {

                log.warn("check local user, in case not found - persist");

            }
        }

        if (true) {
            log.warn("failed oAuth login");
        }
    }
}
