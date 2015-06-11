package com.microsoft.services.orc.core;


import com.microsoft.services.orc.http.OrcResponse;

public class OrcException extends Exception {

    private OrcResponse response;

    public OrcException(OrcResponse response, String message) {
        super(message);
        this.response = response;
    }

    public OrcException(OrcResponse response, Throwable inner) {
        super(inner);
        this.response = response;
    }

    public OrcResponse getResponse() {
        return this.response;
    }
}
