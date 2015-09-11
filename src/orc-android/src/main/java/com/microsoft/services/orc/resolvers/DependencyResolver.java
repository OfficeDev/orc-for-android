package com.microsoft.services.orc.resolvers;

import com.microsoft.services.orc.http.Credentials;
import com.microsoft.services.orc.http.HttpTransport;
import com.microsoft.services.orc.serialization.JsonSerializer;

public class DependencyResolver extends AbstractDependencyResolver {

    public DependencyResolver(Builder builder) {
        super(builder);
    }

    public static class Builder extends AbstractDependencyResolver.Builder {

        public Builder(HttpTransport transport, JsonSerializer serializer, Auth auth) {
            super(transport, serializer, auth);
        }

        @Override
        public DependencyResolver build() {

            return null;
        }
    }

    public Credentials getCredentials() {
        return super.getAuth().getCredentials();
    }

}
