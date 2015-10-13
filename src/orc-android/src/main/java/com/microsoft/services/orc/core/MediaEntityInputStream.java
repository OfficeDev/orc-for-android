package com.microsoft.services.orc.core;

import com.microsoft.services.orc.http.OrcResponse;

import java.io.IOException;
import java.io.InputStream;

/**
 * The type Media entity input stream.
 */
public class MediaEntityInputStream extends InputStream {
    private InputStream internalStream;
    private OrcResponse response;

    /**
     * Instantiates a new Media entity input stream.
     *
     * @param internalStream the internal stream
     * @param response the response
     */
    public MediaEntityInputStream(InputStream internalStream, OrcResponse response) {
        this.internalStream = internalStream;
        this.response = response;
    }

    @Override
    public int read() throws IOException {
        return internalStream.read();

    }

    @Override
    public void close() throws IOException {
        this.internalStream.close();
        this.response.closeStreamedResponse();
    }

    @Override
    public int available() throws IOException {
        return this.internalStream.available();
    }

    @Override
    public boolean markSupported() {
        return this.internalStream.markSupported();
    }

    @Override
    public synchronized void mark(int readlimit) {
        this.internalStream.mark(readlimit);
    }

    @Override
    public int read(byte[] b) throws IOException {
        return this.internalStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return this.internalStream.read(b, off, len);
    }

    @Override
    public synchronized void reset() throws IOException {
        this.internalStream.reset();
    }

    @Override
    public long skip(long n) throws IOException {
        return this.internalStream.skip(n);
    }
}
