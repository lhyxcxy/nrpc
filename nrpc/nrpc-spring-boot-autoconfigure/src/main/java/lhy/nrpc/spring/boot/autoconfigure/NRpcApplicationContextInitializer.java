
package lhy.nrpc.spring.boot.autoconfigure;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

/**
 * 在容器刷新前做的操作
 */
public class NRpcApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
    	applicationContext.addBeanFactoryPostProcessor(new NRpcBeanFactoryPostProcessor());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}
