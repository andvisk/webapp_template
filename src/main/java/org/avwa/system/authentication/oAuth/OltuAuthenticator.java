package org.avwa.system.authentication.oAuth;

import java.util.UUID;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.slf4j.Logger;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;

@Stateless
public class OltuAuthenticator {

    @Inject
    Logger log;

    public void getCode(HttpServletResponse servletResponse, OAuthProviderType provType, String clientId, String scope, String returUrl) {
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
            log.error( e.toString(), e);
        }
    }

    public OAuthResourceResponse getOAuthResourceResponse(String code, OAuthProviderType type, OAuthProvider oAuthProvider) {
        try {
            OAuthClientRequest request = OAuthClientRequest
                    .tokenProvider(type) //
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(oAuthProvider.getClientId()) //
                    .setClientSecret(oAuthProvider.getClientSecret()) //
                    .setRedirectURI(oAuthProvider.getReturnUrl()) //
                    .setCode(code)
                    .buildBodyMessage();

            //create OAuth client that uses custom http client under the hood
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

            if (type.compareTo(OAuthProviderType.FACEBOOK) == 0) {
                GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request, GitHubTokenResponse.class);
                String accessToken = oAuthResponse.getAccessToken();
                Long expiresIn = oAuthResponse.getExpiresIn();
                OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(oAuthProvider.getAccessInfoUrl())
                        .setAccessToken(accessToken).buildQueryMessage();
                OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
                return resourceResponse;
            }
            if (type.compareTo(OAuthProviderType.GOOGLE) == 0) {
                OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request);
                String accessToken = oAuthResponse.getAccessToken();
                Long expiresIn = oAuthResponse.getExpiresIn();
                OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(oAuthProvider.getAccessInfoUrl())
                        .setAccessToken(accessToken).buildQueryMessage();
                OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
                return resourceResponse;
            }

        } catch (Exception e) {
            log.error( e.toString(), e);
        }
        return null;
    }
}
