package lhy.nrpc.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware {  
  
    // Spring应用上下文环境  
    private static ApplicationContext applicationContext;  
  
   
    /** 
     * 获取对象 
     *  
     * @param name 
     * @return Object
     * @throws BeansException 
     */  
    public static Object getBean(String name) throws BeansException {  
        return applicationContext.getBean(name);  
    }


	@Override
	public void setApplicationContext(ApplicationContext tapplicationContext) throws BeansException {
		// TODO Auto-generated method stub
		applicationContext=tapplicationContext;
	}  
}