package com.nycjv321.http;

import com.nycjv321.http.builder.ResponseClientBuilder;
import com.nycjv321.http.client.Client;
import com.nycjv321.http.client.ResponsesClient;
import com.nycjv321.http.exceptions.HttpException;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ResponseClientTest extends ClientTest {

    private ResponsesClient responsesClient;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws Exception {
        super.beforeMethod();
        responsesClient = ResponseClientBuilder.create().build();
    }


    @Test(expectedExceptions = HttpException.class, dependsOnMethods = "unchecked")
    public void checked() {
        String body = "Body Content";
        test(getMockServer(),
                interaction -> getMockServer().when(
                        HttpRequest.request().withMethod("GET").withPath("/")
                ).respond(
                        HttpResponse.response().withBody(body).withStatusCode(400)
                ),
                t -> assertEquals(
                        getResponsesClient()
                                .get("http://127.0.0.1:1080/")
                                .getStatusLine()
                                .getStatusCode(),
                        400
                )
        );
    }


    @Test
    public void unchecked() {
        Client.unchecked(getResponsesClient(), h -> {
            String body = "Body Content";
            test(getMockServer(),
                    interaction -> getMockServer().when(
                            HttpRequest.request().withMethod("GET").withPath("/")
                    ).respond(
                            HttpResponse.response().withBody(body).withStatusCode(400)
                    ),
                    t -> assertEquals(getResponsesClient().get("http://127.0.0.1:1080/").getStatusLine().getStatusCode(), 400));

        });
    }


    private ResponsesClient getResponsesClient() {
        return responsesClient;
    }
}