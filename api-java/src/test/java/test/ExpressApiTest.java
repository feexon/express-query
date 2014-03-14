package test;

import com.lx2j.express.ExpressServlet;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.ServletHandler;

import java.net.URL;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author Administrator
 * @version 1.0 14-3-14,上午10:40
 */
public class ExpressApiTest {
    private Server server = new Server();

    @Before
    public void setUp() throws Exception {
        server.setConnectors(new Connector[]{
                new SocketConnector() {{
                    setPort(9099);
                }}
        });
        server.setHandler(new ServletHandler() {{
            addServletWithMapping(ExpressServlet.class, "/express/query");
        }});
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void query() throws Exception {
        String result = IOUtils.toString(new URL("http://localhost:9099/express/query?type=yunda&postid=1201088260402"));
        assertThat(result, containsString("\"context\""));
    }

    @Test
    public void encoding() throws Exception {
        String result = IOUtils.toString(new URL("http://localhost:9099/express/query?type=yunda&postid=1201088260402"));
        assertThat(result, containsString("广州"));
    }
}
