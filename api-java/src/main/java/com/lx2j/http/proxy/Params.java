package com.lx2j.http.proxy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0 14-3-13,下午4:24
 */
public class Params {
    private List<Param> params = new ArrayList<Param>();

    public Params(HashMap<String, String[]> paramsMap) {
        for (String name : paramsMap.keySet()) {
            String[] values = paramsMap.get(name);
            for (String value : values) {
                params.add(new Param(name, value));
            }
        }
    }

    public String queryString() {
        StringBuilder graph = new StringBuilder();
        for (Iterator<Param> itr = params.iterator(); itr.hasNext(); ) {
            graph.append(itr.next().encode());
            if (itr.hasNext()) {
                graph.append("&");
            }
        }
        return graph.toString();
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

        private String encodeValue() {
            if (value == null) {
                return "";
            }
            try {
                return URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
