package com.nycjv321.http.client;

import com.google.common.base.Strings;
import com.nycjv321.http.Requests;
import com.nycjv321.http.exceptions.UnAuthorizedException;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.function.Supplier;

/**
 * Created by fedora on 12/13/15.
 */
public class MessageBodyClient extends Client {

    public MessageBodyClient(Supplier<CloseableHttpClient> httpClientSupplier, Requests.Timeouts timeouts) {
        super(httpClientSupplier, timeouts);
    }

    private static String consume(HttpResponse response) {
        HttpEntity entity = response.getEntity();

        // Attempt to create an input stream of the content and create its string representation as UTF8
        try (InputStream content = entity.getContent()) {
            return IOUtils.toString(content, "UTF-8").trim();
        } catch (IOException e) {
            return "";
        } finally {
            try {
                EntityUtils.consume(entity);
            } catch (IOException e) {
                if (e.getMessage().contains("Unauthorized")) {
                    throw new UnAuthorizedException(String.format("Request was not authorized by server. See: %s", e));
                }
                return "";
            }
        }
    }

    /**
     * Create an XML Document based provided url resource
     *
     * @param url
     * @return
     */
    public Document getDocument(String url) {
        SAXBuilder jdomBuilder = new SAXBuilder();
        try (StringReader characterStream = new StringReader(get(url))) {
            return jdomBuilder.build(characterStream);
        } catch (JDOMException | IOException e) {
            throw new com.nycjv321.http.exceptions.HttpException(String.format("Error creating document of %s. See: %s", url, e));
        }
    }

    public String get(URL url, Header... headers) {
        return get(url.toString(), headers);
    }

    /**
     * Performs a HTTP GET and return the HTTP Response content in string format. <p> Each request is recorded to the current logger in the following format:
     * "GET ${URL}: ${STATUS_CODE}"
     *
     * @param url a url to get from
     * @return a string representation of the contents of the HTTP Response
     */
    public String get(String url, Header... headers) {
        CloseableHttpClient httpClient = createHttpClient();
        HttpRequestBase httpGet = create(METHOD.GET, url);

        httpGet.setHeaders(headers);

        // Perform a HTTP GET
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            validateResponse(response, url);
            logger.debug(String.format("GET %s: %s", url, response.getStatusLine()));
            return consume(response);
        } catch (IOException e) {
            logger.error(e);
        }
        return "";
    }

    /**
     * Perform a HTTP GET and convert the response body into a JSON Object. <p>
     * Will throw an {@code IllegalStateException} if the response is null or empty
     *
     * @param url a url to GET
     * @return a JSON Object representing the content of the URL
     * @throws JSONException
     */
    public JSONObject getJSON(String url) throws JSONException {
        final String responseBody = get(url);
        if (Strings.isNullOrEmpty(responseBody)) {
            throw new com.nycjv321.http.exceptions.HttpException(String.format("%s returned an empty or null response", url));
        }
        return new JSONObject(responseBody);
    }

}
