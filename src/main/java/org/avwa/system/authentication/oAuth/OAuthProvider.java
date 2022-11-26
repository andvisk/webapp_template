package org.avwa.system.authentication.oAuth;

public class OAuthProvider {

    private String clientId;
    private String clientSecret;
    private String returnUrl;
    private String accessInfoUrl;

    public OAuthProvider(String clientId, String clientSecret, String returnUrl, String accessInfoUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.returnUrl = returnUrl;
        this.accessInfoUrl = accessInfoUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getAccessInfoUrl() {
        return accessInfoUrl;
    }


}
