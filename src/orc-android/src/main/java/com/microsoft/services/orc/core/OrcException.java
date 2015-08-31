package com.microsoft.services.orc.core;


import com.microsoft.services.orc.http.OrcResponse;

/**
 * The type Orc exception.
 */
public class OrcException extends Exception {

    private OrcResponse response;

    /**
     * Instantiates a new ORC exception.
     *
     * @param response the response
     * @param message the message
     */
    public OrcException(OrcResponse response, String message) {
        super(message);
        this.response = response;
    }

    /**
     * Instantiates a new ORC exception.
     *
     * @param response the response
     * @param inner the inner
     */
    public OrcException(OrcResponse response, Throwable inner) {
        super(inner);
        this.response = response;
    }

    /**
     * Gets response.
     *
     * @return the response
     */
    public OrcResponse getResponse() {
        return this.response;
    }
}
