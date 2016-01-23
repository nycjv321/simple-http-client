package com.nycjv321.http.builder;

import com.nycjv321.http.Requests;
import com.nycjv321.http.client.ResponseClient;

import java.util.Objects;

/**
 * Created by fedora on 11/18/15.
 */
public class ResponseClientBuilder extends ClientBuilder {

    private ResponseClientBuilder() {
        super();
    }

    public static ResponseClientBuilder create() {
        return new ResponseClientBuilder();
    }

    @Override
    public ResponseClient build() {
        if (Objects.nonNull(timeouts)) {
            return new ResponseClient(httpClientSupplier, timeouts);
        } else {
            return new ResponseClient(httpClientSupplier, Requests.Timeouts.getDefault());
        }
    }
}
