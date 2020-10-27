package lhy.nrpc.common.utils;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;
import static org.springframework.core.annotation.AnnotationUtils.getAnnotationAttributes;
import static org.springframework.util.ClassUtils.resolveClassName;

import java.lang.annotation.Annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.StringUtils;

import lhy.nrpc.common.annotation.NRpcService;
import lhy.nrpc.common.constant.RpcConstant;



public class ServiceBeanNameUtil {

	private static final String SEPARATOR = ":";
	public static String generateBeanName(BeanDefinition definition, ClassLoader classLoader) {
		
		Class<?>beanclass=resolveClass(definition,classLoader);
		Annotation ann=findServiceAnnotation(beanclass);
		
		AnnotationAttributes attributes = getAnnotationAttributes(ann, false, false);
		 Class<?> interfaceClass = AnnotationUtils.resolveServiceInterfaceClass(attributes, beanclass);
		String beanName=ServiceBeanNameUtil.buildBeanName(interfaceClass.getName(), 
				AnnotationUtils.getAttributeToString(attributes, RpcConstant.RPC_REFERENCE_VERSION), 
				AnnotationUtils.getAttributeToString(attributes, RpcConstant.RPC_REFERENCE_GROUP));
		// Fallback: generate a unique default bean name.
		return beanName;
	}
	
	public static String generateBeanName(AnnotationAttributes attributes, Class<?> interfaceClass) {
	
		String beanName=ServiceBeanNameUtil.buildBeanName(interfaceClass.getName(), 
			AnnotationUtils.getAttributeToString(attributes, RpcConstant.RPC_REFERENCE_VERSION), 
			AnnotationUtils.getAttributeToString(attributes, RpcConstant.RPC_REFERENCE_GROUP));
		return beanName;
	}

	private static Annotation findServiceAnnotation(Class<?> beanClass) {
        Annotation service = findMergedAnnotation(beanClass, NRpcService.class);
        if (service == null) {
            service = findMergedAnnotation(beanClass, NRpcService.class);
        }
        return service;
    }
	
	private  static Class<?> resolveClass(BeanDefinition beanDefinition,ClassLoader classLoader) {

        String beanClassName = beanDefinition.getBeanClassName();

        return resolveClassName(beanClassName, classLoader);

    }
	
	 public static String buildBeanName(String interfaceClassName,String version,String group) {
	        StringBuilder beanNameBuilder = new StringBuilder("ServiceBean");
	        // Required
	        append(beanNameBuilder, interfaceClassName);
	        // Optional
	        append(beanNameBuilder, group);
	        append(beanNameBuilder, version);
	        
	        String rawBeanName = beanNameBuilder.toString();

	        return rawBeanName;
	    }
	 
	 private static void append(StringBuilder builder, String value) {
	        if (StringUtils.hasText(value)) {
	            builder.append(SEPARATOR).append(value);
	        }
	        else
	        	 builder.append(SEPARATOR);
	    }
}
