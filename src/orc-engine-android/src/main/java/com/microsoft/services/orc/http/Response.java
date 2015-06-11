package com.microsoft.services.orc.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * The interface Response.
 */
public interface Response {
    /**
     * Gets headers.
     *
     * @return the headers
     */
    Map<String, List<String>> getHeaders();

    /**
     * Gets headers.
     *
     * @param headerName the header name
     * @return the headers
     */
    List<String> getHeaders(String headerName);

    /**
     * Gets status.
     *
     * @return the status
     */
    int getStatus();

    /**
     * Gets stream.
     *
     * @return the stream
     */
    InputStream getStream();

    /**
     * Close void.
     *
     * @throws IOException the iO exception
     */
    void close() throws IOException;
}
