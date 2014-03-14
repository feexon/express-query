package test;

import com.lx2j.http.route.Router;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.lx2j.http.route.Router.route;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static test.MockSite.*;

/**
 * @author Administrator
 * @version 1.0 14-3-13,下午5:41
 */
public class RouterTest {


    private MockSite site = new MockSite(9099);
    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();

    @Before
    public void startServer() throws Exception {
        site.start();
    }

    @After
    public void stopServer() throws Exception {
        site.stop();
    }

    @Test
    public void transferDataFromSite() throws Exception {
        site.will(respond("ok"));
        route(request, response).to("http://localhost:9099/");
        assertThat(response.getContentAsString(), equalTo("ok"));
    }

    @Test
    public void dispatchParamsFromRequest() throws Exception {
        request.setParameter("type", "yunda");

        site.expect(param("type", "yunda"));

        route(request, response).to("http://localhost:9099/");
    }

    @Test
    public void addAdditionParams() throws Exception {
        site.expect(param("type", "yunda")).expect(param("postid", "1201088260402"));
        request.setParameter("type", "yunda");

        route(request, response).with("postid", "1201088260402").to("http://localhost:9099/");

    }

    @Test
    public void addHeaders() throws Exception {
        site.expect(header("userAgent", "Mozilla"));


        route(request, response).header("userAgent", "Mozilla").to("http://localhost:9099/");

    }

    @Test
    public void should_responseCommitted_raiseException() throws Exception {

        response.setCommitted(true);
        try {
            route(request, response).to("http://localhost:9099/");
            fail("should raise exception");
        } catch (IllegalStateException expected) {

        }

    }

    @Test
    public void routeTo() throws Exception {
        request.setParameter("type","yunda");
        request.setParameter("postid","1201088260402");
        route(request,response).to("http://baidu.kuaidi100.com/query?id=4&valicode=&temp=0.06312353069088816&tmp=0.501010342742183");
        System.out.println(new String(response.getContentAsString().getBytes("ISO-8859-1"),"UTF-8"));
    }
}
