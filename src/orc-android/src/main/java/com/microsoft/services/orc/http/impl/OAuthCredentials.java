package com.microsoft.services.orc.http.impl;


import com.microsoft.services.orc.http.Credentials;
import com.microsoft.services.orc.http.Request;

/**
 * The type OAuth credentials.
 */
public class OAuthCredentials implements Credentials {

    private String mToken;

    /**
     * Instantiates a new OAuth credentials.
     *
     * @param oAuthToken the OAuth token
     */
    public OAuthCredentials(String oAuthToken) {
        mToken = oAuthToken;
    }

    /**
     * Returns the OAuth Token
     * @return the token
     */
    public String getToken() {
        return mToken;
    }

    @Override
    public void prepareRequest(Request request) {
        request.addHeader("Authorization", "Bearer " + getToken());
    }

}
