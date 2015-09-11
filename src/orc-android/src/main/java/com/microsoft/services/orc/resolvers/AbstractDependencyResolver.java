package com.microsoft.services.orc.resolvers;

import android.os.Build;

import com.microsoft.services.orc.core.Constants;
import com.microsoft.services.orc.http.HttpTransport;
import com.microsoft.services.orc.http.OrcURL;
import com.microsoft.services.orc.http.Request;
import com.microsoft.services.orc.http.impl.OrcURLImpl;
import com.microsoft.services.orc.http.impl.RequestImpl;
import com.microsoft.services.orc.log.LoggerBase;
import com.microsoft.services.orc.log.impl.LoggerImpl;
import com.microsoft.services.orc.serialization.JsonSerializer;


/**
 * The type Default dependency resolver.
 */
abstract class AbstractDependencyResolver {

    private Builder builder;

    protected AbstractDependencyResolver(Builder builder) {
        this.builder = builder;
    }

    abstract static class Builder {

        private HttpTransport transport;
        private JsonSerializer jsonSerializer;
        private Auth auth;
        private LoggerBase logger;
        private OrcURL orcURL;
        private Request request;

        protected Builder(HttpTransport transport, JsonSerializer serializer, Auth auth) {
            this.transport = transport;
            this.jsonSerializer = serializer;
            this.auth = auth;
            this.logger = new LoggerImpl();
            this.orcURL = new OrcURLImpl();
            this.request = new RequestImpl();
        }

        public abstract AbstractDependencyResolver build();
    }

    public HttpTransport getHttpTransport() {
        return builder.transport;
    }

    public JsonSerializer getJsonSerializer() {
        return builder.jsonSerializer;
    }

    public  Request createRequest(){
        return builder.request;
    }

    public LoggerBase getLogger() {
        return builder.logger;
    }

    public OrcURL getOrcURL() {
        return builder.orcURL;
    }

    public Request getRequest() {
        return builder.request;
    }

    public Auth getAuth() {
        return builder.auth;
    }

    public String getPlatformUserAgent(String productName) {

        return String.format(
                "%s/1.0 (lang=%s; os=%s; os_version=%s; arch=%s; version=%s)",
                productName, "Java", "Android", Build.VERSION.RELEASE,
                Build.CPU_ABI, Constants.SDK_VERSION);
    }
}
