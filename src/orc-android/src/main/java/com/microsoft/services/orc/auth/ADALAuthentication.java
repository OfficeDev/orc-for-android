package com.microsoft.services.orc.auth;

import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.services.orc.http.Credentials;
import com.microsoft.services.orc.http.impl.OAuthCredentials;

public class ADALAuthentication implements AuthenticationCredentials {

    private AuthenticationContext context;
    private String[] scopes;
    private String clientId;

    public ADALAuthentication(AuthenticationContext context, String[] scopes, String clientId) {
        this.context = context;
        this.scopes = scopes;
        this.clientId = clientId;
    }

    public Credentials getCredentials() {
        AuthenticationResult result = this.context.acquireTokenSilentSync(scopes, clientId, null);
        return new OAuthCredentials(result.getAccessToken());
    }
}
