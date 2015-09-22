/*******************************************************************************
 * Copyright (c) Microsoft Open Technologies, Inc.
 * All Rights Reserved
 * See License.txt in the project root for license information.
 ******************************************************************************/
package com.microsoft.services.orc.core;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.services.orc.http.Credentials;
import com.microsoft.services.orc.http.HttpTransport;
import com.microsoft.services.orc.http.OrcResponse;
import com.microsoft.services.orc.http.Request;
import com.microsoft.services.orc.http.Response;
import com.squareup.okhttp.HttpUrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * The type BaseOrcContainer.
 */
public abstract class BaseOrcContainer extends OrcExecutable {

    Logger logger = LoggerFactory.getLogger(BaseOrcContainer.class);


    private String url;
    private DependencyResolver resolver;


    /**
     * Instantiates a new Base orc container.
     *
     * @param url      the url
     * @param resolver the resolver
     */
    public BaseOrcContainer(String url, DependencyResolver resolver) {
        this.url = url;
        this.resolver = resolver;
    }

    @Override
    protected ListenableFuture<OrcResponse> oDataExecute(final Request request) {
        final SettableFuture<OrcResponse> result = SettableFuture.create();

        try {
            request.getUrl().setBaseUrl(this.url);
            String rawUrl = request.getUrl().toString();
            String fullUrl = HttpUrl.parse(rawUrl).toString();

            String executionInfo = String.format("URL: %s - HTTP VERB: %s", fullUrl, request.getVerb());
            logger.info("Start preparing OData execution for " + executionInfo);

            if (request.getContent() != null) {
                logger.info("With " + request.getContent().length + " bytes of payload");
                logger.info("Payload: " + new String(request.getContent()));
            } else if (request.getStreamedContent() != null) {
                logger.info("With stream of bytes for payload");
            }

            HttpTransport httpTransport = resolver.getHttpTransport();

            String userAgent = resolver.getPlatformUserAgent(this.getClass().getCanonicalName());
            request.addHeader(Constants.USER_AGENT_HEADER, userAgent);
            request.addHeader(Constants.TELEMETRY_HEADER, userAgent);
            if (!request.getHeaders().containsKey(Constants.CONTENT_TYPE_HEADER)) {
                request.addHeader(Constants.CONTENT_TYPE_HEADER, Constants.JSON_CONTENT_TYPE);
            }
            request.addHeader(Constants.ACCEPT_HEADER, Constants.JSON_CONTENT_TYPE);
            request.addHeader(Constants.ODATA_VERSION_HEADER, Constants.ODATA_VERSION);
            request.addHeader(Constants.ODATA_MAXVERSION_HEADER, Constants.ODATA_MAXVERSION);

            if (request.getHeaders() != null) {
                for (String key : request.getHeaders().keySet()) {
                    request.addHeader(key, request.getHeaders().get(key));
                }
            }

            boolean credentialsSet = false;

            Credentials cred = resolver.getCredentials();
            if (cred != null) {
                cred.prepareRequest(request);
                credentialsSet = true;
            }

            if (!credentialsSet) {
                logger.info("Executing request without setting credentials");
            }


            logger.info("Request Headers: ");
            for (String key : request.getHeaders().keySet()) {
                logger.info(key + " : " + request.getHeaders().get(key));
            }

            final ListenableFuture<Response> future = httpTransport.execute(request);
            logger.info("OData request executed");

            Futures.addCallback(future, new FutureCallback<Response>() {

                @Override
                public void onSuccess(Response response) {
                    boolean readBytes = true;
                    if (request.getOptions().get(Request.MUST_STREAM_RESPONSE_CONTENT) != null) {
                        readBytes = false;
                    }

                    OrcResponse orcResponse = new OrcResponseImpl(response);

                    try {
                        logger.info("OData response received");

                        int status = response.getStatus();
                        logger.info("Response Status Code: " + status);

                        if (readBytes) {
                            logger.info("Reading response data...");
                            byte[] data = orcResponse.getPayload();
                            logger.info(data.length + " bytes read from response");
                            logger.info("Response Payload:" + new String(data));
                            try {
                                logger.info("Closing response");
                                response.close();
                            } catch (Throwable t) {
                                logger.info("Error closing response: " + t.toString());
                                result.setException(t);
                                return;
                            }

                        }

                        if (status < 200 || status > 299) {
                            logger.info("Invalid status code. Processing response content as String");
                            String responseData = new String(orcResponse.getPayload(), Constants.UTF8_NAME);
                            String message = "Response status: " + response.getStatus() + "\n" + "Response content: " + responseData;
                            logger.info(message);
                            result.setException(new OrcException(orcResponse, message));
                            return;
                        }
                        result.set(orcResponse);
                    } catch (Throwable t) {
                        logger.info("Unexpected error: " + t.toString());
                        result.setException(new OrcException(orcResponse, t));
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    result.setException(throwable);
                }
            });
        } catch (Throwable t) {
            result.setException(t);
        }
        return result;

    }

    /**
     * Generate parameters payload.
     *
     * @param parameters the parameters
     * @param resolver   the resolver
     * @return the string
     */
    public static String generateParametersPayload(Map<String, Object> parameters, DependencyResolver resolver) {
        return resolver.getJsonSerializer().serialize(parameters);
    }

    @Override
    protected DependencyResolver getResolver() {
        return resolver;
    }
}
