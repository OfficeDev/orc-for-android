package com.microsoft.services.orc.http;

/**
 * The interface Credentials.
 */
public interface Credentials {
    /**
     * Prepare request.
     *
     * @param request the request
     */
    public void prepareRequest(Request request);
}
