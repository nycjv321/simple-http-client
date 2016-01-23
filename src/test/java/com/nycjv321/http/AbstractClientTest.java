package com.nycjv321.http;

import org.mockserver.integration.ClientAndProxy;
import org.mockserver.integration.ClientAndServer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by fedora on 11/18/15.
 */
public abstract class AbstractClientTest {

    private ClientAndProxy proxy;
    private ClientAndServer mockServer;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws Exception {
        mockServer = ClientAndServer.startClientAndServer(1080);
        proxy = ClientAndProxy.startClientAndProxy(1090);

    }


    @AfterMethod(alwaysRun = true)
    public void afterMethod() throws Exception {
        if (Objects.nonNull(getProxy())) {
            getProxy().stop();
        }
        if (Objects.nonNull(getMockServer())) {
            getMockServer().stop();
        }
    }


    public <T extends AbstractClientTest> void test(ClientAndServer mockServer, Consumer<T> mockedInteraction, Consumer<T> test) {
        mockedInteraction.accept((T) this);
        test.accept((T) this);
        mockServer.reset();
    }

    protected ClientAndServer getMockServer() {
        return mockServer;
    }

    public ClientAndProxy getProxy() {
        return proxy;
    }
}
