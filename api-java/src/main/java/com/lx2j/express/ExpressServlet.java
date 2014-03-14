package com.lx2j.express;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.lx2j.http.route.Router.route;

/**
 * @author Administrator
 * @version 1.0 14-3-14,上午10:45
 */
public class ExpressServlet extends HttpServlet {
    private static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final String PARAM_REFERER = "Referer";
    private static final String PARAM_USER_AGENT = "User-Agent";
    private static final String PARAM_SITE = "site";
    private String referer;
    private String site;
    private String userAgent;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        setReferer(get(PARAM_REFERER, "http://baidu.kuaidi100.com/index2.html?bd_user=0&bd_sig=7215b099855fb4735a9f1b5d6f286fd7&canvas_pos=search&keyword=%E5%BF%AB%E9%80%92%E6%9F%A5%E8%AF%A2"));
        setUserAgent(get(PARAM_USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0"));
        setSite(get(PARAM_SITE, "http://baidu.kuaidi100.com/query?id=4&valicode="));
    }

    private String get(String name, String defaultValue) {
        String result = getServletConfig().getInitParameter(name);
        if (result == null) {
            return defaultValue;
        }
        return result;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(JSON_CONTENT_TYPE);
        route(req, resp).
                header("X-Requested-With", "XMLHttpRequest").
                header(PARAM_USER_AGENT, userAgent).header(PARAM_REFERER, referer).
                to(site);
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
