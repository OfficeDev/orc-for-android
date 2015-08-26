package com.microsoft.services.orc.resolvers;

import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.UserIdentifier;
import com.microsoft.services.orc.http.Credentials;
import com.microsoft.services.orc.http.impl.OAuthCredentials;


/**
 * The type ADAL dependency resolver.
 */
public class ADALDependencyResolver extends DefaultDependencyResolver {

    private AuthenticationContext context;
    private String[] scopes;
    private String clientId;

    /**
     * Instantiates a new dependency resolver.
     *
     * @param context  the context
     * @param scopes   the scopes
     * @param clientId the client id
     */
    public ADALDependencyResolver(AuthenticationContext context, String[] scopes, String clientId) {
        super("");
        this.context = context;
        this.scopes = scopes;
        this.clientId = clientId;
    }

    /**
     * Sets scopes.
     *
     * @param scopes the scopes
     */
    public void setScopes(String[] scopes) {
        this.scopes = scopes;
    }

    /**
     * Gets scopes.
     *
     * @return the scopes
     */
    public String[] getScopes() {
        return this.scopes;
    }

    @Override
    public Credentials getCredentials() {
        AuthenticationResult result = this.context.acquireTokenSilentSync(scopes, clientId,
                                           UserIdentifier.getAnyUser());
        return new OAuthCredentials(result.getAccessToken());
    }
}
