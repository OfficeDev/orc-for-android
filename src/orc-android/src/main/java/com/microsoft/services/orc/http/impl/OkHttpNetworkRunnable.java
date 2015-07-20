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
            RequestBody requestBody = null;

            if (mRequest.getContent() != null) {
                requestBody = RequestBody.create(MediaType.parse(""), mRequest.getContent());
            } else if (mRequest.getStreamedContent() != null) {
                final InputStream stream = mRequest.getStreamedContent();
                requestBody = new StreamedRequest(stream);
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
                //close here??
                mFuture.set(new EmptyResponse(status, okResponse.headers().toMultimap()));
            }

        } catch (Throwable t) {
            t.printStackTrace();
            mFuture.setException(t);
        }
    }

    private class StreamedRequest extends RequestBody {
        private final InputStream stream;

        public StreamedRequest(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public MediaType contentType() {
            return null;
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            sink.buffer().readFrom(stream, mRequest.getStreamedContentSize());
        }
    }
}
