package com.nycjv321.http.builder;

import com.nycjv321.http.Requests;
import com.nycjv321.http.client.ResponsesClient;

import java.util.Objects;

/**
 * Created by fedora on 11/18/15.
 */
public class ResponseClientBuilder extends SimpleHttpClientBuilder {

    private ResponseClientBuilder() {
        super();
    }

    public static ResponseClientBuilder create() {
        return new ResponseClientBuilder();
    }

    @Override
    public ResponsesClient build() {
        if (Objects.nonNull(timeouts)) {
            return new ResponsesClient(httpClientSupplier, timeouts);
        } else {
            return new ResponsesClient(httpClientSupplier, Requests.Timeouts.getDefault());
        }
    }
}
