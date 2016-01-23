package com.nycjv321.http;

import com.nycjv321.http.builder.ResponseClientBuilder;
import com.nycjv321.http.client.Client;
import com.nycjv321.http.client.ResponseClient;
import com.nycjv321.http.exceptions.HttpException;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ClientTest extends AbstractClientTest {

    private ResponseClient responseClient;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws Exception {
        super.beforeMethod();
        responseClient = ResponseClientBuilder.create().build();
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
                        getResponseClient()
                                .get("http://127.0.0.1:1080/")
                                .getStatusLine()
                                .getStatusCode(),
                        400
                )
        );
    }


    @Test
    public void unchecked() {
        Client.unchecked(getResponseClient(), h -> {
            String body = "Body Content";
            test(getMockServer(),
                    interaction -> getMockServer().when(
                            HttpRequest.request().withMethod("GET").withPath("/")
                    ).respond(
                            HttpResponse.response().withBody(body).withStatusCode(400)
                    ),
                    t -> assertEquals(getResponseClient().get("http://127.0.0.1:1080/").getStatusLine().getStatusCode(), 400));

        });
    }


    private ResponseClient getResponseClient() {
        return responseClient;
    }
}