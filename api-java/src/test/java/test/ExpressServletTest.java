package test;

import com.lx2j.express.ExpressServlet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import test.support.MockSiteRule;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertThat;
import static test.support.MockSite.header;

/**
 * @author Administrator
 * @version 1.0 14-3-14,上午11:02
 */
public class ExpressServletTest {
    private static final int SERVER_PORT = 9099;
    private static final String SITE_URL = "http://localhost:" + SERVER_PORT + "/express/query";
    @Rule
    public MockSiteRule site = new MockSiteRule(SERVER_PORT);

    private ExpressServlet servlet = new ExpressServlet();

    @Before
    public void setUp() {
        servlet.setSite(SITE_URL);
    }

    @Test
    public void referer() throws Exception {
        servlet.setReferer("http://baidu.kuaidi100.com");

        site.expect(header("Referer", "http://baidu.kuaidi100.com"));

        query();
    }

    private void query() throws ServletException, IOException {
        servlet.service(new MockHttpServletRequest("GET", "/express/query"), new MockHttpServletResponse());
    }

    @Test
    public void userAgent() throws Exception {
        servlet.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");

        site.expect(header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0"));

        query();
    }

    @Test
    public void requestedWithXMLHTTPRequest() throws Exception {
        site.expect(header("X-Requested-With", "XMLHttpRequest"));
        query();
    }

    @Test
    public void init() throws Exception {
        servlet.setSite(null);
        servlet.init(new MockServletConfig() {{
            addInitParameter("User-Agent", "IE");
            addInitParameter("Referer", "http://www.google.com");
            addInitParameter("site", SITE_URL);
        }});

        site.expect(header("User-Agent", "IE")).expect(header("Referer", "http://www.google.com"));
        query();
    }
}
