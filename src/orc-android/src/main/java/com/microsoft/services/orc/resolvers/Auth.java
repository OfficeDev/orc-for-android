package com.microsoft.services.orc.resolvers;

import com.microsoft.services.orc.http.Credentials;

interface Auth {

    Credentials getCredentials();

}
