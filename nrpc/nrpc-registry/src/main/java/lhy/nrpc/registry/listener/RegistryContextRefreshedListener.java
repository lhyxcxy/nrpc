package lhy.nrpc.registry.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import lhy.nrpc.registry.thread.RegisterHeartbeatCheckThread;
@Component
public class RegistryContextRefreshedListener  implements ApplicationListener<ContextRefreshedEvent> ,DisposableBean{
    
    protected static final Logger LOGGER = LoggerFactory.getLogger(RegistryContextRefreshedListener.class);
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent ev) {
        //防止重复执行。
        if(ev.getApplicationContext().getParent() == null){
        	RegisterHeartbeatCheckThread.start();
             
        }
    }

	@Override
	public void destroy() throws Exception {
		RegisterHeartbeatCheckThread.toStop();
		
	}
 
}