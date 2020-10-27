package lhy.nrpc.core.proxy;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.InjectionMetadata.InjectedElement;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationAttributes;

import lhy.nrpc.common.annotation.NRpcReference;
import lhy.nrpc.common.constant.RpcConstant;
import lhy.nrpc.common.utils.ReferenceBeanNameUtil;
import lhy.nrpc.common.utils.ServiceBeanNameUtil;

/**
 * 此类在自动注入属性时会被调用，获得所有属性及方法上有@Reference注解的元数据，
 * 然后得到对其进行动态代理后的对象，并注入到属性中，同时自定义bean的名字（name+version）并把其注入到fectoryBean。
 * 当然也可以保存在本地方便清除
 * @Description:   
 * @author: lhy 
 * @date:   2020年8月6日 下午6:42:44   
 *
 */

public class ReferenceAnnotationBeanPostProcessor extends AnnotationInjectedBeanPostProcessor implements DisposableBean{
	/**
     * The bean name of {@link ReferenceAnnotationBeanPostProcessor}
     */
    public static final String BEAN_NAME = "nrpcReferenceAnnotationBeanPostProcessor";

	private RpcReferenceBeanFactory rpcReferenceBeanFactory;
	private static  final ConcurrentMap<String,Object> needServiceIds = new ConcurrentHashMap<>(CACHE_SIZE);
	public ReferenceAnnotationBeanPostProcessor(
			RpcReferenceBeanFactory rpcReferenceBeanFactory) {
		super(NRpcReference.class);
		this.rpcReferenceBeanFactory = rpcReferenceBeanFactory;
	}


    
	

	@Override
	protected Object doGetInjectedBean(AnnotationAttributes attributes,
			Class<?> injectedType, InjectedElement injectedElement) throws Exception {
		String serviceBeanName = ServiceBeanNameUtil.generateBeanName(attributes, injectedType);

        Object referenceBean = buildReferenceBeanIfAbsent(serviceBeanName,injectedType,attributes);
        needServiceIds.put(serviceBeanName, "");
        return referenceBean;
	}

	private Object buildReferenceBeanIfAbsent(String serviceBeanName,Class<?> injectedType,AnnotationAttributes attributes)
		throws Exception {
		ConfigurableListableBeanFactory  factory=super.getBeanFactory();
		if(factory.containsBean(serviceBeanName))
			return factory.getBean(serviceBeanName);
		else
		{ 
			Object version =getAttribute(attributes,RpcConstant.RPC_REFERENCE_VERSION);
			Object loadbalance=getAttribute(attributes, RpcConstant.RPC_REFERENCE_LOADBALANCE);
			Object rpctimeout=getAttribute(attributes, RpcConstant.RPC_REFERENCE_TIMEOUT);
			String tversion=(version==null?"":version.toString());
			String tloadbalance=(loadbalance==null?"":version.toString());
			long trpctimeout=(rpctimeout==null?RpcReferenceBeanFactory.DEFAULT_TIMEOUT:Long.valueOf(rpctimeout.toString()));
			 Object bean= rpcReferenceBeanFactory.getRpcReferenceBean(serviceBeanName,injectedType, 
					 tloadbalance
					,trpctimeout);
			registerReferenceBean(serviceBeanName, bean, attributes, injectedType);
			return bean;
		}
		
	}

	/**
     * Register an instance of {@link ReferenceBean} as a Spring Bean
     *
     * @param serviceBeanName the referenced bean name
     * @param referenceBean      the instance of {@link ReferenceBean}
     * @param attributes         the {@link AnnotationAttributes attributes} of {@link Reference @Reference}
     * @param interfaceClass     the {@link Class class} of Service interface
     * @since 2.7.3
     */
    private void registerReferenceBean(String serviceBeanName, Object referenceBean,
                                       AnnotationAttributes attributes,
                                       Class<?> interfaceClass) {

        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        String beanName = ReferenceBeanNameUtil.getReferenceBeanName(attributes, interfaceClass);

        if (beanFactory.containsBean(serviceBeanName)) { // If @Service bean is local one
            /**
             * Get  the @Service's BeanDefinition from {@link BeanFactory}
             * Refer to {@link ServiceAnnotationBeanPostProcessor#buildServiceBeanDefinition}
             *//*
            AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) beanFactory.getBeanDefinition(referencedBeanName);
            RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference) beanDefinition.getPropertyValues().get("ref");
            // The name of bean annotated @Service
            String serviceBeanName = runtimeBeanReference.getBeanName();
            // register Alias rather than a new bean name, in order to reduce duplicated beans
*/            beanFactory.registerAlias(serviceBeanName, beanName);
        } else { // Remote @Service Bean
            if (!beanFactory.containsBean(beanName)) {
                beanFactory.registerSingleton(beanName, referenceBean);
            }
        }
    }
    

	
	@Override
	protected String buildInjectedObjectCacheKey(AnnotationAttributes attributes,
			Class<?> injectedType) {
		// TODO Auto-generated method stub
		return ReferenceBeanNameUtil.getReferenceBeanName(attributes, injectedType);
	}
	
	
	

	
	public static Object getAttribute(AnnotationAttributes attributes,String name){
	 return attributes.get(name);
	}
	
	   public static Set<String>getNeedServiceIds(){
	    	return needServiceIds.keySet();
	    }
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		super.destroy();
		needServiceIds.clear();
	}
}