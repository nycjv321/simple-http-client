package com.nycjv321.http.builder;

import com.nycjv321.http.Requests;
import com.nycjv321.http.client.MessageBodyClient;

import java.util.Objects;

/**
 * Created by fedora on 11/18/15.
 */
public class MessageBodyClientBuilder extends SimpleHttpClientBuilder {


    private MessageBodyClientBuilder() {
        super();
    }

    public static MessageBodyClientBuilder create() {
        return new MessageBodyClientBuilder();
    }

    @Override
    public MessageBodyClient build() {
        if (Objects.nonNull(timeouts)) {
            return new MessageBodyClient(httpClientSupplier, timeouts);
        } else {
            return new MessageBodyClient(httpClientSupplier, Requests.Timeouts.getDefault());
        }
    }
}
