package com.microsoft.services.orc.http.impl;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import okio.BufferedSink;

public class ActionInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        if (request.body() == null && request.method().equalsIgnoreCase("POST")) {

            Request patchedRequest = new Request.Builder().url(request.httpUrl().toString())
                    .method(request.method(), empty(request.body()))
                    .headers(request.headers())
                    .build();

            return chain.proceed(patchedRequest);

        } else {
            return chain.proceed(request);
        }

    }

    private RequestBody empty(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.buffer().write("{}".getBytes());
            }
        };
    }

}
