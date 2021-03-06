package com.nycjv321.http;

import com.google.common.collect.ImmutableMap;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by fedora on 11/21/15.
 */
public class HttpQueryParameterBuilderTest {

    @Test
    public void build() throws Exception {
        Map<String, String> data = ImmutableMap.of("key1", "value1", "key2", "value2", "key3", "value3");
        String expected = "?key1=value1&key2=value2&key3=value3";
        String actual = HttpQueryParameterBuilder.build(data);
        assertEquals(actual, expected);
    }
    @Test
    public void buildEmpty() throws Exception {
        Map<String, String> data = ImmutableMap.of();
        assertTrue(HttpQueryParameterBuilder.build(data).isEmpty());
    }
}