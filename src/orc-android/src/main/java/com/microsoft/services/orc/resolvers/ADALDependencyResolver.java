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
    private AuthenticationResult result;

    /**
     * Instantiates a new dependency resolver.
     *
     * @param context  the context
     * @param scopes   the scopes
     * @param clientId the client id
     */
    public ADALDependencyResolver(AuthenticationContext context, AuthenticationResult result,
                                  String[] scopes, String clientId) {
        super("");
        this.context = context;
        this.scopes = scopes;
        this.clientId = clientId;
        this.result = result;
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
                new UserIdentifier(getUniqueId(), UserIdentifier.UserIdentifierType.UniqueId));
        return new OAuthCredentials(result.getAccessToken());
    }

    private String getUniqueId() {
        if (result != null && result.getUserInfo() != null
                && result.getUserInfo().getUniqueId() != null) {
            return result.getUserInfo().getUniqueId();
        }
        throw new IllegalStateException("cannot get Unique Id");
    }
}
