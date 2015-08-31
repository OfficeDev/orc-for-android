package com.microsoft.services.orc.core;

/**
 * The type MultiPart element.
 */
public class MultiPartElement {
    private static final String HTML_CONTENT_TYPE = "text/html";
    private String name;
    private String contentType;
    private byte[] content;

    /**
     * Instantiates a new MultiPart element.
     *
     * @param name the name
     * @param content the content
     */
    public MultiPartElement(String name, String content) {
        this(name, HTML_CONTENT_TYPE, content.getBytes(Constants.UTF8));
    }

    /**
     * Instantiates a new Multi part element.
     *
     * @param name the name
     * @param contentType the content type
     * @param content the content
     */
    public MultiPartElement(String name, String contentType, byte[] content) {
        this.name = name;
        this.contentType = contentType;

        if (content == null) {
            throw new IllegalArgumentException("content");
        }
        this.content = content;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets content type.
     *
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets content type.
     *
     * @param contentType the content type
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Get content.
     *
     * @return the byte [ ]
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(byte[] content) {
        this.content = content;
    }
}
