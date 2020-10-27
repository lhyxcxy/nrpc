/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lhy.nrpc.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

import lhy.nrpc.core.proxy.ReferenceAnnotationBeanPostProcessor;
import lhy.nrpc.core.proxy.RpcReferenceBeanFactory;
import lhy.nrpc.core.proxy.s.ServiceAnnotationBeanPostProcessor;
import lhy.nrpc.core.registry.RemoteConsumerRegistry;
import lhy.nrpc.core.registry.RemoteProviderRegistry;
import lhy.nrpc.core.utils.SpringContextUtil;

import static lhy.nrpc.common.constant.RpcConstant.NRPC_CONFIG_PREFIX;
/**
 * Dubbo Auto {@link Configuration}
 *
 * @see Reference
 * @see Service
 * @see ServiceAnnotationBeanPostProcessor
 * @see ReferenceAnnotationBeanPostProcessor
 * @since 2.7.0
 */

@ConditionalOnProperty(prefix = NRPC_CONFIG_PREFIX,name = "enabled",havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(NRpcConfigurationProperties.class)
public class NRpcAutoConfiguration {

	
	@ConditionalOnMissingBean
	@ConditionalOnBean(NRpcConfigurationProperties.class)
	@ConditionalOnProperty(name = NRPC_CONFIG_PREFIX+".scan.basePackages")
    @Bean(name = ServiceAnnotationBeanPostProcessor.BEAN_NAME)
    public ServiceAnnotationBeanPostProcessor serviceAnnotationBeanPostProcessor(NRpcConfigurationProperties nrpcConfigurationProperties){
        return new ServiceAnnotationBeanPostProcessor(nrpcConfigurationProperties.getScan().getBasePackages());
    }

    /**
     * Creates {@link ReferenceAnnotationBeanPostProcessor} Bean if Absent
     *
     * @return {@link ReferenceAnnotationBeanPostProcessor}
     */
    //@ConditionalOnBean(RpcReferenceBeanFactory.class)
    @ConditionalOnMissingBean
    @Bean(name = ReferenceAnnotationBeanPostProcessor.BEAN_NAME)
    public ReferenceAnnotationBeanPostProcessor referenceAnnotationBeanPostProcessor(RpcReferenceBeanFactory referenceBeanFactory) {
       return new ReferenceAnnotationBeanPostProcessor(referenceBeanFactory);
    }
    
    @ConditionalOnMissingBean
    @Bean
    public RpcReferenceBeanFactory rpcReferenceBeanFactory() {
        return new RpcReferenceBeanFactory();
    }
    
    
    @ConditionalOnMissingBean
    @Bean
    public RemoteProviderRegistry remoteProviderRegistry(NRpcConfigurationProperties nrConfigurationProperties){
    	return new RemoteProviderRegistry(nrConfigurationProperties.getRegistry(),nrConfigurationProperties.getProvider(),nrConfigurationProperties.getNrpcnode());
    }
    
    @ConditionalOnMissingBean
    @Bean
    public RemoteConsumerRegistry remoteConsumerRegistry(NRpcConfigurationProperties nrConfigurationProperties){
    	return new RemoteConsumerRegistry(nrConfigurationProperties.getRegistry(),nrConfigurationProperties.getConsumer(),nrConfigurationProperties.getNrpcnode());
    }
    
   // @ConditionalOnBean({RemoteConsumerRegistry.class,RemoteProviderRegistry.class,NRpcConfigurationProperties.class})
    @ConditionalOnMissingBean(NRpcContextRefreshedListener.class)
    @Bean
    public NRpcContextRefreshedListener nrpcContextRefreshedListener(){
    	return new NRpcContextRefreshedListener();
    }
    
    @ConditionalOnMissingBean
    @Bean
    public SpringContextUtil springContextUtil(){
    	return new SpringContextUtil();
    }
    
    @Bean
    @Primary
    public PropertyResolver primaryPropertyResolver(Environment environment) {
        return environment;
    }
}
