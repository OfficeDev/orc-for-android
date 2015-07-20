package com.microsoft.services.orc.resolvers;

import android.os.Build;

import com.microsoft.services.orc.core.Constants;
import com.microsoft.services.orc.core.DependencyResolver;
import com.microsoft.services.orc.http.Credentials;
import com.microsoft.services.orc.http.HttpTransport;
import com.microsoft.services.orc.http.OrcURL;
import com.microsoft.services.orc.http.Request;
import com.microsoft.services.orc.http.impl.OkHttpTransport;
import com.microsoft.services.orc.http.impl.OrcURLImpl;
import com.microsoft.services.orc.http.impl.RequestImpl;
import com.microsoft.services.orc.log.Logger;
import com.microsoft.services.orc.log.LoggerBase;
import com.microsoft.services.orc.log.impl.LoggerImpl;
import com.microsoft.services.orc.log.impl.SimpleLogger;
import com.microsoft.services.orc.serialization.JsonSerializer;
import com.microsoft.services.orc.serialization.impl.GsonSerializer;

public class OkHttpDependencyResolver implements DependencyResolver {

    private LoggerBase logger;
    private String token;

    /**
     * Instantiates a new Default dependency resolver.
     */
    public OkHttpDependencyResolver(String token) {
        this.token = token;
        this.logger = new SimpleLogger();
    }

    @Override
    public HttpTransport getHttpTransport() {
        return new OkHttpTransport();
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public JsonSerializer getJsonSerializer() {
        return new GsonSerializer();
    }

    @Override
    public OrcURL createODataURL() {
        return new OrcURLImpl();
    }

    @Override
    public Request createRequest() {
        return new RequestImpl();
    }

    @Override
    public String getPlatformUserAgent(String productName) {

        return String.format(
                "%s/1.0 (lang=%s; os=%s; os_version=%s; arch=%s; version=%s)",
                productName, "Java", "Android", Build.VERSION.RELEASE,
                Build.CPU_ABI, Constants.SDK_VERSION);
    }

    @Override
    public Credentials getCredentials() {
        return new Credentials() {
            @Override
            public void prepareRequest(Request request) {
                request.addHeader("Authorization", "Bearer " + token);
            }
        };
    }
}