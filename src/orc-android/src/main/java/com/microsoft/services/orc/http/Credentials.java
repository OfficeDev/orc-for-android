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
    void prepareRequest(Request request);
}
