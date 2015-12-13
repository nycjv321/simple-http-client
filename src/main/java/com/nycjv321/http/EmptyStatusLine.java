package com.nycjv321.http;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.jetbrains.annotations.Nullable;

/**
 * Created by fedora on 12/13/15.
 */
public class EmptyStatusLine implements StatusLine {

    private static final HttpResponse EMPTY_RESPONSE;

    static {
        EMPTY_RESPONSE = new BasicHttpResponse(new EmptyStatusLine());
    }

    private EmptyStatusLine() {
    }

    public static HttpResponse get() {
        return EMPTY_RESPONSE;
    }

    @Override
    public @Nullable ProtocolVersion getProtocolVersion() {
        return null;
    }

    @Override
    public int getStatusCode() {
        return 0;
    }

    @Override
    @Nullable
    public String getReasonPhrase() {
        return null;
    }

}
