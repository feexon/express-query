package com.lx2j.http.route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0 14-3-13,下午5:45
 */
public class Router {
    private static final int BUFFER_SIZE = 1024 * 3;
    private static final int NAME_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    private HttpServletResponse response;
    private Params params;
    private List<String[]> headers = new ArrayList<String[]>();

    public Router(HttpServletRequest request, HttpServletResponse response) {
        this.response = response;
        params = new Params().merge(request.getParameterMap());
    }

    public static Router route(HttpServletRequest request, HttpServletResponse response) {
        return new Router(request, response);
    }

    public void to(String site) throws IOException {
        InputStream in = connect(site).getInputStream();
        try {
            transmit(in);
        } finally {
            close(in);
        }
    }

    private URLConnection connect(String site) throws IOException {
        if (response.isCommitted()) {
            throw new IllegalStateException("Response committed!");
        }

        return apply(params.join(URI.create(site)).toURL().openConnection());
    }

    private URLConnection apply(URLConnection connection) {
        for (String[] header : headers) {
            connection.setRequestProperty(header[NAME_INDEX], header[VALUE_INDEX]);
        }
        return connection;
    }

    private void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ignore) {
            }
        }
    }

    private void transmit(InputStream in) throws IOException {
        OutputStream out = response.getOutputStream();
        try {
            join(in, out);
        } finally {
            close(out);
        }
    }

    private void join(InputStream in, OutputStream out) throws IOException {
        byte[] buff = new byte[BUFFER_SIZE];
        while (true) {
            int count = in.read(buff);
            if (count == -VALUE_INDEX) {
                break;
            }
            out.write(buff, NAME_INDEX, count);
        }
    }

    public Router with(String name, String... values) {
        params.with(name, values);
        return this;
    }

    public Router header(String name, String value) {
        headers.add(new String[]{name, value});
        return this;
    }
}
