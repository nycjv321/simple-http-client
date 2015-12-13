package com.nycjv321.http.client;

import com.nycjv321.http.Requests;
import com.nycjv321.http.exceptions.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by fedora on 11/18/15.
 */
public class Client {
    protected final Logger logger = LogManager.getLogger(getClass());
    private final Requests.Timeouts timeouts;
    private final Supplier<CloseableHttpClient> httpClientSupplier;
    private boolean validateResponse = true;

    Client(Supplier<CloseableHttpClient> httpClientSupplier, Requests.Timeouts timeouts) {
        this.timeouts = timeouts;
        this.httpClientSupplier = httpClientSupplier;
    }

    public synchronized static void unchecked(Client http, Consumer<Consumer<?>> h) {
        boolean originalState = http.validateResponse;
        http.validateResponse = false;
        h.accept(h);
        http.validateResponse = originalState;
    }

    @SuppressWarnings("unchecked")
    protected <T extends HttpRequestBase> T create(METHOD method, String url) {
        HttpRequestBase httpRequest;
        switch (method) {
            case HEAD:
                httpRequest = new HttpHead(url);
                break;
            case GET:
                httpRequest = new HttpGet(url);
                break;
            case POST:
                httpRequest = new HttpPost(url);
                break;
            case PUT:
                httpRequest = new HttpPut(url);
                break;
            default:
                throw new IllegalArgumentException(String.format("%s was invalid Http Method", method));
        }

        configureRequestTimeouts(httpRequest);
        return (T) httpRequest;
    }

    private void configureRequestTimeouts(HttpRequestBase request) {
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(timeouts.getSocketTimeout())
                .setConnectTimeout(timeouts.getConnectTimeout())
                .setConnectionRequestTimeout(timeouts.getConnectionRequestTimeout())
                .build();
        request.setConfig(config);
    }


    protected CloseableHttpClient createHttpClient() {
        return httpClientSupplier.get();
    }

    /**
     * If {@code validateResponse} and the response is a 401 throw a {@code UnAuthorizedException}
     *
     * @param response a response to check
     * @param url      a url that was requested to generate the response
     */
    protected void validateResponse(HttpResponse response, String url) {
        if (validateResponse) {
            if (response.getStatusLine().getStatusCode() >= 400) {
                throw new HttpException(String.format("Got %s for %s", response.getStatusLine(), url));
            }
        }
    }
}