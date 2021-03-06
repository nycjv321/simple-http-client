package com.nycjv321.http.client;

import com.nycjv321.http.EmptyStatusLine;
import com.nycjv321.http.Requests;
import com.nycjv321.utilities.FileUtilities;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.function.Supplier;

/**
 * Created by fedora on 12/13/15.
 */
public final class ResponseClient extends Client {

    public ResponseClient(Supplier<CloseableHttpClient> httpClientSupplier, Requests.Timeouts timeouts) {
        super(httpClientSupplier, timeouts);
    }

    /**
     * Performs a HTTP PUT and return the HTTP Status Line. <p> Each request is recorded to the current logger in the following format:
     * "PUT ${URL}: ${STATUS_CODE}"
     *
     * @param url     a url to PUT to
     * @param body    a strings representing the request entity
     * @param headers a set of additional headers necessary to the request
     * @return a string representation of the contents of the HTTP Status Code
     */
    public HttpResponse put(String url, String body, Header... headers) {
        final File randomFileInTemp = FileUtilities.getRandomFileInTemp();
        try {
            FileUtils.writeStringToFile(randomFileInTemp, body);
            return put(url, randomFileInTemp, headers);
        } catch (IOException e) {
            return EmptyStatusLine.get();
        } finally {
            randomFileInTemp.deleteOnExit();
        }
    }

    /**
     * Performs a HTTP PUT and return the HTTP Status Line. <p> Each request is recorded to the current logger in the following format:
     * "PUT ${URL}: ${STATUS_CODE}"
     *
     * @param url     a url to PUT to
     * @param file    the request entity
     * @param headers a set of additional headers necessary to the request
     * @return a string representation of the contents of the HTTP Status Code
     */
    public HttpResponse put(String url, File file, Header... headers) {
        CloseableHttpClient httpClient = createHttpClient();
        HttpPut httpPut = create(METHOD.PUT, url);

        FileEntity reqEntity = new FileEntity(file);
        httpPut.setEntity(reqEntity);
        httpPut.setHeaders(headers);

        try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
            validateResponse(response, url);
            final StatusLine statusLine = response.getStatusLine();
            logger.debug(String.format("PUTing \"%s\" to %s: %s", file.getAbsoluteFile(), url, statusLine));
            return getOriginal(response);
        } catch (IOException e) {
            logger.error(e);
            return EmptyStatusLine.get();
        }
    }

    /**
     * See {@code HttpUtilities#head(String url}
     *
     * @param uri
     * @return
     */
    public HttpResponse head(URI uri) {
        return head(uri.toString());
    }

    /**
     * Performs a HTTP HEAD and return the HTTP Status Line. <p> Each request is recorded to the current logger in the following format:
     * "HEAD ${URL}: ${STATUS_CODE}"
     *
     * @param url a url to HEAD to
     * @return a string representation of the contents of the HTTP Status Code
     */
    public HttpResponse head(String url) {
        CloseableHttpClient httpClient = createHttpClient();
        HttpRequestBase httpHead = create(METHOD.HEAD, url);
        // Perform a HTTP HEAD
        try (CloseableHttpResponse response = httpClient.execute(httpHead)) {
            validateResponse(response, url);
            validateResponse(response, url);
            final StatusLine statusLine = response.getStatusLine();
            logger.debug(String.format("HEAD %s: %s", url, statusLine));
            return getOriginal(response);
        } catch (IOException e) {
            logger.error(e);
        }
        return EmptyStatusLine.get();
    }

    public HttpResponse get(String url, Header... headers) {
        CloseableHttpClient httpClient = createHttpClient();
        HttpRequestBase httpGet = create(METHOD.GET, url);
        httpGet.setHeaders(headers);
        // Perform a HTTP GET
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            validateResponse(response, url);
            logger.debug(String.format("GET %s: %s", url, response.getStatusLine()));
            return response;
        } catch (IOException e) {
            logger.error(e);
        }
        throw new com.nycjv321.http.exceptions.HttpException(String.format("Could not get %s", url));
    }

    public HttpResponse get(URL url) {
        return get(url.toString());
    }

    public HttpResponse post(String url, Header... headers) {
        return post(url, "", headers);
    }

    /**
     * Post a String and return the Response Status Line. <p> Each request is recorded to the current logger in the following format:
     * "POSTing ${FILE} to ${URL}: ${STATUS_LINE}"
     *
     * @param url     a url to POST
     * @param body    a string representing the request body
     * @param headers any headers to associate with the request
     * @return a string representation of the contents of the HTTP Response
     */
    public HttpResponse post(String url, String body, Header... headers) {
        final File randomFileInTemp = FileUtilities.getRandomFileInTemp();
        try {
            FileUtils.writeStringToFile(randomFileInTemp, body);
            return post(url, randomFileInTemp, headers);
        } catch (IOException e) {
            return EmptyStatusLine.get();
        } finally {
            randomFileInTemp.deleteOnExit();
        }
    }

    /**
     * Post a file and return the Response Status Line. <p> Each request is recorded to the current logger in the following format:
     * "POSTing ${FILE} to ${URL}: ${STATUS_LINE}"
     *
     * @param url     a url to POST to
     * @param file    a file representing the post entity
     * @param headers any headers to associate with the request
     * @return a string representation of the contents of the HTTP Response
     */
    public HttpResponse post(String url, File file, Header... headers) {
        CloseableHttpClient httpClient = createHttpClient();
        HttpPost httppost = create(METHOD.POST, url);

        FileEntity reqEntity = new FileEntity(file);
        reqEntity.setChunked(true);
        httppost.setEntity(reqEntity);
        httppost.setHeaders(headers);
        try (CloseableHttpResponse response = httpClient.execute(httppost)) {
            validateResponse(response, url);
            final StatusLine statusLine = response.getStatusLine();
            logger.debug(String.format("POSTing \"%s\" to %s: %s", file.getAbsoluteFile(), url, statusLine));
            return getOriginal(response);
        } catch (IOException e) {
            logger.error(e);
            return EmptyStatusLine.get();
        }
    }

    /**
     * Extract the original Http Response to get the response headers and other goodies.
     *
     * @param closeableHttpResponse
     * @return
     */
    private HttpResponse getOriginal(CloseableHttpResponse closeableHttpResponse) {
        try {
            final Field original = closeableHttpResponse.getClass().getDeclaredField("original");
            original.setAccessible(true);
            return (HttpResponse) original.get(closeableHttpResponse);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return closeableHttpResponse;
        }
    }

}
