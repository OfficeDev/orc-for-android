package com.microsoft.services.android.impl;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.live.LiveAuthClient;
import com.microsoft.live.LiveAuthException;
import com.microsoft.live.LiveAuthListener;
import com.microsoft.live.LiveConnectSession;
import com.microsoft.live.LiveStatus;
import com.microsoft.services.orc.impl.OAuthCredentials;
import com.microsoft.services.orc.interfaces.Credentials;

import java.util.Arrays;
import java.util.List;

/**
 * The type LiveSDK dependency resolver.
 */
public class LiveDependencyResolver extends DefaultDependencyResolver {

    private final List<String> scopes;
    private LiveAuthClient liveAuthClient;
    private Context mContext;

    /**
     * Instantiates a new dependency resolver.
     * @param context the context
     * @param clientId the client id
     * @param scopes the scopes for LiveSDK auth
     *
     */
    public LiveDependencyResolver(Context context, String clientId, String[] scopes) {
        super("");
        this.liveAuthClient = new LiveAuthClient(context, clientId);
        this.scopes = Arrays.asList(scopes);
        this.mContext = context;
    }

    @Override
    public Credentials getCredentials() {
        final SettableFuture<Credentials> credentialsFuture = SettableFuture.create();

        Handler mainHandler = new Handler(mContext.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                liveAuthClient.initialize(scopes, new LiveAuthListener() {
                    @Override
                    public void onAuthError(LiveAuthException exception, Object userState) {
                        credentialsFuture.setException(exception);
                    }

                    @Override
                    public void onAuthComplete(LiveStatus status, LiveConnectSession session, Object userState) {
                        if (status == LiveStatus.CONNECTED) {
                            OAuthCredentials credentials = new OAuthCredentials(session.getAccessToken());
                            credentialsFuture.set(credentials);
                        } else {
                            credentialsFuture.setException(new LiveAuthException("Couldn't initialize LiveAuthClient, perform UI Login."));
                        }
                    }
                });
            }
        });

        try {
            return credentialsFuture.get();
        }catch(LiveAuthException e){
            throw e;
        }catch (Throwable t) {
            Log.e("odata-engine-impl", t.getMessage());
            throw new RuntimeException(t);
        }
    }
}
