package com.microsoft.services.orc.http;

/**
 * The interface Base 64 encoder.
 */
public interface Base64Encoder {
    /**
     * Encode string.
     *
     * @param data the data
     * @return the string
     */
    String encode(byte[] data);

    /**
     * Decode byte array.
     *
     * @param base64String the base 64 string
     * @return the byte array
     */
    byte[] decode(String base64String);
}
