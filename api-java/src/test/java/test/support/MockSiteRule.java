package test.support;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Administrator
 * @version 1.0 14-3-14,上午11:21
 */
public class MockSiteRule extends MockSite implements TestRule {

    public MockSiteRule(int serverPort) {
        super(serverPort);
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    start();
                    base.evaluate();
                    verify();
                } finally {
                    stop();
                }
            }
        };
    }
}
