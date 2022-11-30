package org.avwa.system.authentication.oAuth;

public enum OAuthProviderType {
    FACEBOOK("https://www.facebook.com/v15.0/dialog/oauth", "https://graph.facebook.com/v15.0/oauth/access_token",
            "https://graph.facebook.com/me", "", "Facebook"),
    GOOGLE("https://accounts.google.com/o/oauth2/v2/auth", "https://oauth2.googleapis.com/token",
            "https://www.googleapis.com/oauth2/v2/userinfo", "https://www.googleapis.com/auth/userinfo.email", "Google");

    private String getCodeUri;
    private String accessTokenUri;
    private String accessResourceUri;
    private String scope;
    private String label;


    OAuthProviderType(String getCodeUri, String accessTokenUri, String accessResourceUri, String scope, String label) {
        this.getCodeUri = getCodeUri;
        this.accessTokenUri = accessTokenUri;
        this.accessResourceUri = accessResourceUri;
        this.scope = scope;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getGetCodeUri() {
        return getCodeUri;
    }

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public String getAccessResourceUri() {
        return accessResourceUri;
    }

    public String getScope() {
        return scope;
    }

}
