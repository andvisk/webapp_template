package org.avwa.system.authentication.oAuth;

import java.io.IOException;

import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.avwa.system.authentication.oAuth.accountInfo.OAccountInfo;
import org.slf4j.Logger;

import com.google.gson.Gson;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Stateless
public class ReturnServletUtil {

    @Inject
    Logger log;

    @Inject
    OltuAuthenticator oltuAuthenticator;

    @PersistenceContext
    private EntityManager em;

    public void doGet(HttpServletRequest request, HttpServletResponse response, OAuthProviderType oAuthProviderType)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code != null) {
            try {

                OAuthProvider oAuthProvider = SecurityConfiguration.getProvider(em, oAuthProviderType);

                OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
                String codeFromServer = oar.getCode();

                String body = oltuAuthenticator.getOAuthResourceResponse(
                        codeFromServer, oAuthProviderType, oAuthProvider).getBody();

                OAccountInfo accountInfo = new Gson().fromJson(body, OAccountInfo.class);

                log.warn("check for existing user or else ceate new user");

            } catch (Exception e) {
                log.error(e.toString(), e);
            }
        }
        if (true) {
            log.warn("failed oAuth login");
        }
    }
}
