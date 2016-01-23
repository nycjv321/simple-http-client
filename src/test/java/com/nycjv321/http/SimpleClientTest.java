package com.nycjv321.http;

import com.google.common.collect.ImmutableMap;
import com.nycjv321.http.builder.SimpleClientBuilder;
import com.nycjv321.http.client.SimpleHttpClient;
import com.nycjv321.utilities.XMLUtilities;
import org.apache.http.message.BasicHeader;
import org.jdom2.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class SimpleClientTest extends AbstractClientTest {

    private SimpleHttpClient simpleHttpClient;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws Exception {
        super.beforeMethod();
        simpleHttpClient = SimpleClientBuilder.create().build();
    }

    @Test
    public void get() throws Exception {
        String body = "Body Content";
        test(getMockServer(),
                interaction -> getMockServer().when(
                        HttpRequest.request().withMethod("GET").withPath("/")
                ).respond(
                        HttpResponse.response().withBody(body).withStatusCode(200)
                ),
                t -> assertEquals(getSimpleHttpClient().get("http://127.0.0.1:1080/"), body));
    }

    @Test
    public void getHeaders() throws Exception {
        String body = "Body Content";
        test(getMockServer(),
                interaction -> {
                    getMockServer().when(
                            HttpRequest.request().withMethod("GET").withPath("/").withHeader(new Header("test", "value"))
                    ).respond(
                            HttpResponse.response().withBody(body).withStatusCode(200)
                    );
                },
                t -> assertEquals(getSimpleHttpClient().get("http://127.0.0.1:1080/", new BasicHeader("test", "value")), body));
    }


    @Test(dependsOnMethods = "get")
    public void getDocument() throws Exception {
        Document body = XMLUtilities.toDocument("<root><children><child><id>0</id></child></children></root>");
        test(getMockServer(),
                interaction -> getMockServer().when(
                        HttpRequest.request().withMethod("GET").withPath("/")
                ).respond(
                        HttpResponse.response().withBody(XMLUtilities.toString(body)).withStatusCode(200)
                ),
                t -> assertEquals(XMLUtilities.toString(getSimpleHttpClient().getDocument("http://127.0.0.1:1080/")), XMLUtilities.toString(body)));
    }

    @Test(dependsOnMethods = "getDocument")
    public void getJson() {
        JSONObject jsonObject = new JSONObject(ImmutableMap.of("key1", "value1", "key2", "value2"));
        test(getMockServer(),
                interaction -> getMockServer().when(
                        HttpRequest.request().withMethod("GET").withPath("/")
                ).respond(
                        HttpResponse.response().withBody(jsonObject.toString()).withStatusCode(200)
                ),
                t -> {
                    JSONObject json = null;
                    try {
                        json = getSimpleHttpClient().getJSON("http://127.0.0.1:1080/");
                        assertEquals(json.toString(), jsonObject.toString());
                    } catch (JSONException e) {
                        fail(e.getMessage());
                    }
                });
    }

    protected SimpleHttpClient getSimpleHttpClient() {
        return simpleHttpClient;
    }
}