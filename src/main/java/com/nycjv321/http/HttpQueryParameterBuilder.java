package com.nycjv321.http;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by fedora on 11/21/15.
 */
public class HttpQueryParameterBuilder {

    public static String build(Map<String, String> parameters) {
            if (parameters.isEmpty()) {
                return "";
            }
            Set<String> strings = parameters.keySet();
            String parametersString = "?";
            for (Iterator<String> iterator = strings.iterator(); iterator.hasNext(); ) {
                String string = iterator.next();
                parametersString = parametersString + string + "=" + parameters.get(string);
                if (iterator.hasNext()) {
                    parametersString = parametersString + "&";
                }
            }
            return parametersString;
    }
}
