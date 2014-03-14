package test;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

/**
 * @author Administrator
 * @version 1.0 14-3-13,下午6:13
 */
class MockSite {
    private Server server = new Server();
    private List<Expectation> expectations = new ArrayList<Expectation>();
    private Action action = respond("");

    public MockSite(final int serverPort) {
        server.setConnectors(new Connector[]{
                new SocketConnector() {{
                    setPort(serverPort);
                }}
        });
        server.setHandlers(new Handler[]{
                new AbstractHandler() {

                    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
                        assertSatisfied(request, response);
                        action.execute(request, response);
                    }

                    private void assertSatisfied(HttpServletRequest request, HttpServletResponse response) {
                        for (Expectation expectation : expectations) {
                            try {
                                expectation.check(request, response);
                            } catch (Exception error) {
                                throw new AssertionError(error);
                            } catch (AssertionError error) {
                                throw error;
                            }
                        }
                    }
                }
        });
    }

    public void start() throws Exception {
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public void will(Action action) {
        this.action = action;
    }

    public static Action respond(final String result) {
        return new Action() {

            public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
                PrintWriter out = response.getWriter();
                out.write(result);
                out.close();
            }
        };
    }

    public static interface Expectation {
        void check(HttpServletRequest request, HttpServletResponse response) throws Exception;
    }

    public static interface Action {
        void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
    }

    public MockSite expect(Expectation expectation) {
        expectations.add(expectation);
        return this;
    }

    public static Expectation param(final String name, final String value) {
        return new Expectation() {
            @Override
            public void check(HttpServletRequest request, HttpServletResponse response) throws Exception {
                assertThat(request.getParameter(name), equalTo(value));
            }
        };
    }

    public static Expectation header(final String name, final String value) {
        return new Expectation() {
            @Override
            public void check(HttpServletRequest request, HttpServletResponse response) throws Exception {
                assertThat(request.getHeader(name), equalTo(value));
            }
        };
    }

}
