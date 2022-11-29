package org.avwa.system.authentication.oAuth;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;

@Stateless
public class OltuAuthenticator {

    @Inject
    Logger log;

    public void getCode(HttpServletResponse servletResponse, OAuthProviderType provType, String clientId, String scope,
            String returUrl) {
        try {
            final UUID uuid = UUID.randomUUID();
            String state = uuid.toString();
            OAuthClientRequest request = OAuthClientRequest
                    .authorizationProvider(provType)
                    .setClientId(clientId)
                    .setRedirectURI(returUrl)
                    .setResponseType(ResponseType.CODE.toString())
                    .setScope(scope)
                    .setState(state)
                    .buildQueryMessage();
            if (!servletResponse.isCommitted())
                servletResponse.sendRedirect(request.getLocationUri());
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }

    public OAuthResourceResponse getOAuthResourceResponse(String code, OAuthProviderType type,
            OAuthProvider oAuthProvider) {
        try {

            if (type.compareTo(OAuthProviderType.FACEBOOK) == 0) {

                String requestUri = "https://graph.facebook.com/v15.0/oauth/access_token?client_id=" +
                        oAuthProvider.getClientId() +
                        "&client_secret=" + oAuthProvider.getClientSecret() +
                        "&redirect_uri=" + URLEncoder.encode(oAuthProvider.getReturnUrl(),"UTF-8") +
                        "&client_id=" + oAuthProvider.getClientId() +
                        "&code=" + code;
                String responseJson = new URLConnClient().execute(requestUri, null, OAuth.HttpMethod.GET, null);

                log.debug("request facebook:" + requestUri);
                log.debug("response from facebook:" + responseJson);

                HashMap<String, String> map = new Gson().fromJson(responseJson,
                        new TypeToken<HashMap<String, String>>() {
                        }.getType());

                String access_token = map.get("access_token");

                OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(oAuthProvider.getAccessInfoUrl())
                        .setAccessToken(access_token).buildQueryMessage();

                responseJson = new URLConnClient().execute(bearerClientRequest.getLocationUri(), null, OAuth.HttpMethod.GET, null);
                log.debug("request facebook:" + bearerClientRequest.getLocationUri());
                log.debug("response from facebook:" + responseJson);

                OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
                OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET,
                        OAuthResourceResponse.class);
                return resourceResponse;
            }
            
            if (type.compareTo(OAuthProviderType.GOOGLE) == 0) {

                String requestUri = "https://graph.facebook.com/v15.0/oauth/access_token?client_id=" +
                        oAuthProvider.getClientId() +
                        "&client_secret=" + oAuthProvider.getClientSecret() +
                        "&redirect_uri=" + URLEncoder.encode(oAuthProvider.getReturnUrl(),"UTF-8") +
                        "&client_id=" + oAuthProvider.getClientId() +
                        "&code=" + code;
                String responseJson = new URLConnClient().execute(requestUri, "GET", null, null);

                log.debug("request facebook:" + requestUri);
                log.debug("response from facebook:" + responseJson);

                HashMap<String, String> map = new Gson().fromJson(responseJson,
                        new TypeToken<HashMap<String, String>>() {
                        }.getType());

                String access_token = map.get("access_token");

                OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(oAuthProvider.getAccessInfoUrl())
                        .setAccessToken(access_token).buildQueryMessage();

                responseJson = new URLConnClient().execute(bearerClientRequest.getLocationUri(), "GET", null, null);
                log.debug("request facebook:" + bearerClientRequest.getLocationUri());
                log.debug("response from facebook:" + responseJson);

                OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
                OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET,
                        OAuthResourceResponse.class);
                return resourceResponse;
            }
            // create OAuth client that uses custom http client under the hood

            /*
             * OAuthClient oAuthClient = new OAuthClient(new URLConnClient());
             * if (type.compareTo(OAuthProviderType.FACEBOOK) == 0) {
             * 
             * GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request,
             * GitHubTokenResponse.class);
             * String accessToken = oAuthResponse.getAccessToken();
             * Long expiresIn = oAuthResponse.getExpiresIn();
             * OAuthClientRequest bearerClientRequest = new
             * OAuthBearerClientRequest(oAuthProvider.getAccessInfoUrl())
             * .setAccessToken(accessToken).buildQueryMessage();
             * OAuthResourceResponse resourceResponse =
             * oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET,
             * OAuthResourceResponse.class);
             * return resourceResponse;
             * }
             * if (type.compareTo(OAuthProviderType.GOOGLE) == 0) {
             * OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request);
             * String accessToken = oAuthResponse.getAccessToken();
             * Long expiresIn = oAuthResponse.getExpiresIn();
             * OAuthClientRequest bearerClientRequest = new
             * OAuthBearerClientRequest(oAuthProvider.getAccessInfoUrl())
             * .setAccessToken(accessToken).buildQueryMessage();
             * OAuthResourceResponse resourceResponse =
             * oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET,
             * OAuthResourceResponse.class);
             * return resourceResponse;
             * }
             */

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return null;
    }
}
