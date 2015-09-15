package com.microsoft.services.orc.resolvers;

import com.microsoft.services.orc.http.Credentials;
import com.microsoft.services.orc.http.HttpTransport;
import com.microsoft.services.orc.serialization.JsonSerializer;

public class DependencyResolver extends AbstractDependencyResolver {

    public DependencyResolver(Builder builder) {
        super(builder);
    }

    public DependencyResolver(HttpTransport transport, JsonSerializer serializer, Auth auth) {
        this(new Builder(transport, serializer, auth));
    }

    public static class Builder extends AbstractDependencyResolver.Builder {

        public Builder(HttpTransport transport, JsonSerializer serializer, Auth auth) {
            super(transport, serializer, auth);
        }

        public Builder setHttpTransport(HttpTransport transport) {
            return (Builder) super.setHttpTransport(transport);
        }

        public Builder setJsonSerializer(JsonSerializer jsonSerializer) {
            return (Builder) super.setJsonSerializer(jsonSerializer);
        }

        public Builder setAuth(Auth auth) {
            return (Builder) super.setAuth(auth);
        }

        @Override
        public DependencyResolver build() {
            return new DependencyResolver(this);
        }
    }

    public Credentials getCredentials() {
        return super.getAuth().getCredentials();
    }

}
