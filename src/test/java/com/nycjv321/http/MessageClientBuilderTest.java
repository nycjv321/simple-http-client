package com.nycjv321.http;

import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by fedora on 11/18/15.
 */
public class MessageClientBuilderTest extends MessageClientTest {

    @Test
    public void build() {
        String body = "Body Content";
        test(getMockServer(),
                interaction -> getMockServer().when(
                        HttpRequest.request().withMethod("GET").withPath("/")
                ).respond(
                        HttpResponse.response().withBody(body).withStatusCode(200)
                ),
                t -> assertEquals(getMessageBodyClient().get("http://127.0.0.1:1080/"), body));
    }

}