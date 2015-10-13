package com.microsoft.services.orc.core;

import com.microsoft.services.orc.http.OrcResponse;

import java.io.IOException;
import java.io.InputStream;

public class ODataStream extends InputStream {

    private InputStream inputStream;
    private OrcResponse response;

    public ODataStream(InputStream inputStream, OrcResponse response) {

        this.inputStream = inputStream;
        this.response = response;
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        response.closeStreamedResponse();
    }
}
