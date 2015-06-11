package com.microsoft.services.orc.http;

public interface Base64Encoder {
    String encode(byte[] data);
    byte[] decode(String base64String);
}
