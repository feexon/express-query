package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import test.support.MockSite;
import test.support.MockSiteRule;

import static com.lx2j.http.route.Router.route;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static test.support.MockSite.*;

/**
 * @author Administrator
 * @version 1.0 14-3-13,下午5:41
 */
public class RouterTest {


    private static final int SERVER_PORT = 9099;
    private static final String SITE = "http://localhost:" + SERVER_PORT + "/";
    @Rule
    public MockSiteRule site = new MockSiteRule(SERVER_PORT);
    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();

    @Test
    public void transferDataFromSite() throws Exception {
        site.will(respond("ok"));
        route(request, response).to(SITE);
        assertThat(response.getContentAsString(), equalTo("ok"));
    }

    @Test
    public void dispatchParamsFromRequest() throws Exception {
        request.setParameter("type", "yunda");

        site.expect(param("type", "yunda"));

        route(request, response).to(SITE);
    }

    @Test
    public void addAdditionParams() throws Exception {
        site.expect(param("type", "yunda")).expect(param("postid", "1201088260402"));
        request.setParameter("type", "yunda");

        route(request, response).with("postid", "1201088260402").to(SITE);

    }

    @Test
    public void addHeaders() throws Exception {
        site.expect(header("userAgent", "Mozilla"));


        route(request, response).header("userAgent", "Mozilla").to(SITE);

    }

    @Test
    public void should_responseCommitted_raiseException() throws Exception {

        response.setCommitted(true);
        try {
            route(request, response).to(SITE);
            fail("should raise exception");
        } catch (IllegalStateException expected) {

        }

    }

}
