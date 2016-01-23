package com.nycjv321.http.builder;

import com.nycjv321.http.Requests;
import com.nycjv321.http.client.SimpleHttpClient;

import java.util.Objects;

/**
 * Created by fedora on 11/18/15.
 */
public class SimpleClientBuilder extends ClientBuilder {

    private SimpleClientBuilder() {
        super();
    }

    public static SimpleClientBuilder create() {
        return new SimpleClientBuilder();
    }

    @Override
    public SimpleHttpClient build() {
        if (Objects.nonNull(timeouts)) {
            return new SimpleHttpClient(httpClientSupplier, timeouts);
        } else {
            return new SimpleHttpClient(httpClientSupplier, Requests.Timeouts.getDefault());
        }
    }
}
