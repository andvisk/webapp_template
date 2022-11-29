package org.avwa.system.authentication.oAuth;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletResponse;

@Stateful
public class OAuth {

    @Inject
    Logger log;

    @PersistenceContext
    private EntityManager em;

    final static String GET = "GET";
    final static String POST = "POST";
    final static String PUT = "PUT";

    private String state = null;

    public void getCode(HttpServletResponse servletResponse, OAuthProviderType providerType) {
        try {

            OAuthProvider oAuthProvider = SecurityConfiguration.getProvider(em, providerType);

            final UUID uuid = UUID.randomUUID();
            state = uuid.toString();

            String uri = providerType.getGetCodeUri() + "?" + String.format(
                    "prompt=consent&response_type=code&client_id=%1$s&redirect_uri=%2$s&state=%3$s&scope=%4$s",
                    oAuthProvider.getClientId(),
                    URLEncoder.encode(oAuthProvider.getReturnUrl(), "UTF-8"),
                    state,
                    URLEncoder.encode(providerType.getScope(), "UTF-8"));

            if (!servletResponse.isCommitted())
                servletResponse.sendRedirect(uri);
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }

    public Map<String, String> getResources(String code, OAuthProviderType providerType) {

        //todo check state

        OAuthProvider oAuthProvider = SecurityConfiguration.getProvider(em, providerType);

        try {
            String requestUri = providerType.getAccessTokenUri();

            String body = "client_id=" + oAuthProvider.getClientId() +
                    "&client_secret=" + oAuthProvider.getClientSecret() +
                    "&redirect_uri=" + oAuthProvider.getReturnUrl() +
                    "&client_id=" + oAuthProvider.getClientId() +
                    "&grant_type=authorization_code&code=" + code;

            String responseJson = new URLConnClient().execute(requestUri, POST, body, null);

            log.debug("request:" + requestUri);
            log.debug("response:" + responseJson);

            HashMap<String, String> map = new Gson().fromJson(responseJson,
                    new TypeToken<HashMap<String, String>>() {
                    }.getType());

            String access_token = map.get("access_token");

            requestUri = providerType.getAccessResourceUri() +
                    "?fields=id,name,email&access_token=" + access_token;

            responseJson = new URLConnClient().execute(requestUri, GET, null, null);

            log.debug("request:" + requestUri);
            log.debug("response:" + responseJson);

            map = new Gson().fromJson(responseJson,
                    new TypeToken<HashMap<String, String>>() {
                    }.getType());

            return map;

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return null;
    }
}