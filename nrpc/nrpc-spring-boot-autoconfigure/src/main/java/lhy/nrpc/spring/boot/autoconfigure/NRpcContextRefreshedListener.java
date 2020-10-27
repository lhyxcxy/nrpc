package lhy.nrpc.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.StringUtils;

import lhy.nrpc.common.serializer.HessianSerializer;
import lhy.nrpc.core.config.ProviderConfig;
import lhy.nrpc.core.config.RegistryConfig;
import lhy.nrpc.core.proxy.s.ServiceAnnotationBeanPostProcessor;
import lhy.nrpc.core.registry.RegisterTask;
import lhy.nrpc.core.registry.RemoteConsumerRegistry;
import lhy.nrpc.core.registry.RemoteProviderRegistry;
import lhy.nrpc.core.rpc.server.Nserver;

public class NRpcContextRefreshedListener  implements ApplicationListener<ContextRefreshedEvent> {
    
    protected static final Logger LOGGER = LoggerFactory.getLogger(NRpcContextRefreshedListener.class);
    @Autowired
    private NRpcConfigurationProperties config;
    @Autowired
    private RemoteProviderRegistry providerRegistry;
    @Autowired
	private RemoteConsumerRegistry consumerRegistry;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent ev) {
        //防止重复执行。
    	if(providerRegistry!=null)
        if(ev.getApplicationContext().getParent() == null){
         try {
        	 ProviderConfig pconfig=config.getProvider();
        	 RegistryConfig registry=config.getRegistry();
        	 if(pconfig!=null &&!StringUtils.isEmpty(pconfig.getHost())&&pconfig.getPort()!=null)
        		 if(!StringUtils.isEmpty(registry.getAddresses())&&registry.getAddresses().size()>0)
        			 if(ServiceAnnotationBeanPostProcessor.getServiceBeanIds().size()>0)
        			 {
        				 	new  Nserver(pconfig,new HessianSerializer()).start();
        				 	
        			 }
        	 new RegisterTask(providerRegistry,consumerRegistry).start();
        	 providerRegistry.resetAboutLocalRferenceAliveProviders();
        	 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
             
        }
    }
 
}