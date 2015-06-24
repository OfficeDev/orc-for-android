package com.microsoft.services.orc.http;

import java.io.InputStream;
import java.util.Map;

/**
 * The interface Request.
 */
public interface Request {

    String MUST_STREAM_RESPONSE_CONTENT = "MUST_STREAM_RESPONSE_CONTENT";

    /**
     * Sets content.
     *
     * @param content the content
     */
    void setContent(byte[] content);


    /**
     * Sets content that comes from a stream
     * @param stream the stream
     */
    void setStreamedContent(InputStream stream, long streamSize);

    /**
     * Get the streamed content
     * @return the stream
     */
    InputStream getStreamedContent();

    /**
     * Get the streamed content size
     * @return the size
     */
    long getStreamedContentSize();

    /**
     * Get content.
     *
     * @return the byte [ ]
     */
    byte[] getContent();

    /**
     * Gets headers.
     *
     * @return the headers
     */
    Map<String, String> getHeaders();

    /**
     * Sets headers.
     *
     * @param headers the headers
     */
    void setHeaders(Map<String, String> headers);

    /**
     * Add header.
     *
     * @param name the name
     * @param value the value
     */
    void addHeader(String name, String value);

    /**
     * Remove header.
     *
     * @param name the name
     */
    void removeHeader(String name);

    /**
     * Gets verb.
     *
     * @return the verb
     */
    HttpVerb getVerb();

    /**
     * Sets verb.
     *
     * @param httpVerb the http verb
     */
    void setVerb(HttpVerb httpVerb);

    /**
     * Sets url.
     *
     * @param url the url
     */
    void setUrl(OrcURL url);

    /**
     * Gets url.
     *
     * @return the url
     */
    OrcURL getUrl();

    Map<String, String> getOptions();

    void addOption(String option, String value);
}