package com.lx2j.http.route;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0 14-3-13,下午4:24
 */
public class Params {
    private List<Param> params = new ArrayList<Param>();

    public Params merge(Map<String, String[]> paramsMap) {
        for (String name : paramsMap.keySet()) {
            with(name, paramsMap.get(name));
        }
        return this;
    }

    public Params with(String name, Object... values) {
        if (values == null) {
            return add(name, null);
        }
        for (Object value : values) {
            add(name, value);
        }
        return this;
    }

    private Params add(String name, Object value) {
        params.add(new Param(name, value == null ? "" : String.valueOf(value)));
        return this;
    }

    public String queryString() {
        return join(params.toArray());
    }

    public URI join(URI origin) {
        try {
            return new URI(origin.getScheme(), origin.getUserInfo(), origin.getHost(), origin.getPort(), origin.getPath(), query(origin), origin.getFragment());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String query(URI origin) {
        return containingQueryString(origin) ? join(origin.getQuery(), queryString()) : queryString();
    }

    private String join(Object... params) {
        if (params.length == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(params[0]);
        for (int i = 1; i < params.length; i++) {
            result.append("&").append(params[i]);
        }
        return result.toString();
    }

    private boolean containingQueryString(URI origin) {
        return origin.getQuery() != null && origin.getQuery().length() > 1;
    }

    private static class Param {
        private String name;
        private String value;

        private Param(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String encode() {
            return name + "=" + encodeValue();
        }

        public String toString() {
            return encode();
        }

        private String encodeValue() {
            try {
                return URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
