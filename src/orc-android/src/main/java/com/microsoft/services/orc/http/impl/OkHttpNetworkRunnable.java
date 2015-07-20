package com.microsoft.services.orc.http.impl;

import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.services.orc.http.NetworkRunnable;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSink;


public class OkHttpNetworkRunnable extends NetworkRunnable {
    public OkHttpNetworkRunnable(com.microsoft.services.orc.http.Request request,
                                 SettableFuture<com.microsoft.services.orc.http.Response> future) {
        super(request, future);
    }

    @Override
    public void run() {

        try {

            OkHttpClient client = new OkHttpClient();
            client.networkInterceptors().add(new LoggingInterceptor());

            RequestBody requestBody = null;
            MediaType mediaType = MediaType.parse("application/json");

            if (mRequest.getContent() != null) {
                requestBody = RequestBody.create(mediaType, mRequest.getContent());
            } else if (mRequest.getStreamedContent() != null) {
                requestBody = new StreamedRequest(mediaType, mRequest);
            }

            Request request = new Request.Builder().url(mRequest.getUrl().toString())
                    .method(mRequest.getVerb().toString(), requestBody)
                    .headers(Headers.of(mRequest.getHeaders()))
                    .build();

            Response okResponse = client.newCall(request).execute();
            int status = okResponse.code();
            final ResponseBody responseBody = okResponse.body();
            InputStream stream = null;

            if (requestBody != null) {
                stream = responseBody.byteStream();
            }

            if (stream != null) {
                Closeable closeable = new Closeable() {
                    @Override
                    public void close() throws IOException {
                        responseBody.close();
                    }
                };
                com.microsoft.services.orc.http.Response response = new ResponseImpl(
                        stream, status, okResponse.headers().toMultimap(), closeable);

                mFuture.set(response);
            } else {
                responseBody.close();
                mFuture.set(new EmptyResponse(status, okResponse.headers().toMultimap()));
            }

        } catch (Throwable t) {
            t.printStackTrace();
            mFuture.setException(t);
        }
    }

    private class StreamedRequest extends RequestBody {

        private MediaType mediaType;
        private com.microsoft.services.orc.http.Request request;

        public StreamedRequest(MediaType mediaType, com.microsoft.services.orc.http.Request request) {
            this.mediaType = mediaType;
            this.request = request;
        }

        @Override
        public MediaType contentType() {
            return mediaType;
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            sink.buffer().readFrom(request.getStreamedContent(), request.getStreamedContentSize());
        }
    }
}
