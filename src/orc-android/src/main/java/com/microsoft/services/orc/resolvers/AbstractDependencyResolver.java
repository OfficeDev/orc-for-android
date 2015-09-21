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

abstract class AbstractDependencyResolver {

    private Builder builder;

    protected AbstractDependencyResolver(Builder builder) {
        this.builder = builder;
    }

    abstract static class Builder {

        private HttpTransport transport;
        private JsonSerializer jsonSerializer;
        private AuthenticationCredentials auth;
        private LoggerBase logger;

        protected Builder(HttpTransport transport, JsonSerializer serializer, AuthenticationCredentials auth) {
            this.transport = transport;
            this.jsonSerializer = serializer;
            this.auth = auth;
            this.logger = new LoggerImpl();
        }

        public abstract AbstractDependencyResolver build();

        public Builder setHttpTransport(HttpTransport transport) {
            this.transport = transport;
            return this;
        }

        public Builder setJsonSerializer(JsonSerializer jsonSerializer) {
            this.jsonSerializer = jsonSerializer;
            return this;
        }

        public Builder setAuth(AuthenticationCredentials auth) {
            this.auth = auth;
            return this;
        }
    }

    public HttpTransport getHttpTransport() {
        return builder.transport;
    }

    public JsonSerializer getJsonSerializer() {
        return builder.jsonSerializer;
    }

    public Request createRequest() {
        return new RequestImpl();
    }

    public LoggerBase getLogger() {
        return builder.logger;
    }

    public OrcURL getOrcURL() {
        return new OrcURLImpl();
    }

    public Request getRequest() {
        return new RequestImpl();
    }

    public AuthenticationCredentials getAuth() {
        return builder.auth;
    }

    public String getPlatformUserAgent(String productName) {

        return String.format(
                "%s/1.0 (lang=%s; os=%s; os_version=%s; arch=%s; version=%s)",
                productName, "Java", "Android", Build.VERSION.RELEASE,
                Build.CPU_ABI, Constants.SDK_VERSION);
    }
}
