import com.lx2j.http.proxy.Params;
import org.junit.Test;

import java.net.URI;
import java.util.LinkedHashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Administrator
 * @version 1.0 14-3-13,下午4:16
 */
public class ParamsTest {

    @Test
    public void noParams() throws Exception {
        Params params = new Params();
        params.merge(new LinkedHashMap<String, String[]>());
        assertThat(params.queryString(), equalTo(""));
    }

    @Test
    public void one() throws Exception {
        Params params = new Params();
        params.merge(new LinkedHashMap<String, String[]>() {{
            put("type", new String[]{"yunda"});
        }});
        assertThat(params.queryString(), equalTo("type=yunda"));
    }

    @Test
    public void flattenParams() throws Exception {
        Params params = new Params();
        params.merge(new LinkedHashMap<String, String[]>() {{
            put("type", new String[]{"yunda"});
            put("postid", new String[]{"1201088260402"});
        }});
        assertThat(params.queryString(), equalTo("type=yunda&postid=1201088260402"));
    }

    @Test
    public void encodeSpecialChars() throws Exception {
        Params params = new Params().merge(new LinkedHashMap<String, String[]>() {{
            put("type", new String[]{"yun da"});
        }});
        assertThat(params.queryString(), equalTo("type=yun+da"));
    }

    @Test
    public void withinValues() throws Exception {
        Params params = new Params().merge(new LinkedHashMap<String, String[]>() {{
            put("ranges", new String[]{"1", "2"});
        }});

        assertThat(params.queryString(), equalTo("ranges=1&ranges=2"));
    }

    @Test
    public void treatNullValueAsEmptyString() throws Exception {
        Params params = new Params();
        params.merge(new LinkedHashMap<String, String[]>() {{
            put("type", new String[]{null});
        }});

        assertThat(params.queryString(), equalTo("type="));
    }

    @Test
    public void mixedParams() throws Exception {
        Params params = new Params();
        params.merge(new LinkedHashMap<String, String[]>() {{
            put("type", new String[]{"yunda"});
            put("ranges", new String[]{"1", "2"});
        }});
        assertThat(params.queryString(), equalTo("type=yunda&ranges=1&ranges=2"));
    }

    @Test
    public void add() throws Exception {
        Params params = new Params();
        params.with("type", "yunda");
        assertThat(params.queryString(), equalTo("type=yunda"));
    }

    @Test
    public void withNull() throws Exception {
        Params params = new Params();
        params.with("type", null);
        assertThat(params.queryString(), equalTo("type="));
    }

    @Test
    public void withValues() throws Exception {
        Params params = new Params();
        params.with("ranges", "1", "2");
        assertThat(params.queryString(), equalTo("ranges=1&ranges=2"));
    }

    @Test
    public void joinURIWithoutQueryString() throws Exception {
        Params params = new Params();
        params.with("type", "yunda");
        assertThat(params.join(URI.create("http://baidu.kaidi100.com/kuaidi/query.jsp")).toString(), equalTo("http://baidu.kaidi100.com/kuaidi/query.jsp?type=yunda"));
        assertThat(params.join(URI.create("http://baidu.kaidi100.com/kuaidi/query.jsp?")).toString(), equalTo("http://baidu.kaidi100.com/kuaidi/query.jsp?type=yunda"));
    }

    @Test
    public void joinURI() throws Exception {
        Params params = new Params();
        params.with("type", "yunda");
        URI joined = params.join(URI.create("http://baidu.kaidi100.com/kuaidi/query.jsp?postid=1201088260402"));
        assertThat(joined.toString(), equalTo("http://baidu.kaidi100.com/kuaidi/query.jsp?postid=1201088260402&type=yunda"));
    }
}