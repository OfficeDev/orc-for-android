package com.microsoft.services.orc.auth;

import com.microsoft.services.orc.http.Credentials;

public interface AuthenticationCredentials {

    Credentials getCredentials();

}
