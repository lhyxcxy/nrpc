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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import lhy.nrpc.core.config.ConsumerConfig;
import lhy.nrpc.core.config.NRpcNodeConfig;
import lhy.nrpc.core.config.ProtocolConfig;
import lhy.nrpc.core.config.ProviderConfig;
import lhy.nrpc.core.config.RegistryConfig;

import java.util.LinkedHashSet;
import java.util.Set;
import static lhy.nrpc.common.constant.RpcConstant.NRPC_CONFIG_PREFIX;

@ConfigurationProperties(NRPC_CONFIG_PREFIX)
public class NRpcConfigurationProperties {

    
    @NestedConfigurationProperty
    private RegistryConfig registry = new RegistryConfig();

    @NestedConfigurationProperty
    private ProtocolConfig protocol = new ProtocolConfig();


    @NestedConfigurationProperty
    private ProviderConfig provider = new ProviderConfig();

    @NestedConfigurationProperty
    private ConsumerConfig consumer = new ConsumerConfig();
    
    @NestedConfigurationProperty
    private NRpcNodeConfig nrpcnode = new NRpcNodeConfig();
    @NestedConfigurationProperty
    private Scan scan = new Scan();
   

    public RegistryConfig getRegistry() {
        return registry;
    }

    public void setRegistry(RegistryConfig registry) {
        this.registry = registry;
    }

    public ProtocolConfig getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolConfig protocol) {
        this.protocol = protocol;
    }

    
    public ProviderConfig getProvider() {
        return provider;
    }

    public void setProvider(ProviderConfig provider) {
        this.provider = provider;
    }

    public ConsumerConfig getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerConfig consumer) {
        this.consumer = consumer;
    }
    
  
    public Scan getScan() {
		return scan;
	}

	public void setScan(Scan scan) {
		this.scan = scan;
	}
	

	public NRpcNodeConfig getNrpcnode() {
		return nrpcnode;
	}

	public void setNrpcnode(NRpcNodeConfig nrpcnode) {
		this.nrpcnode = nrpcnode;
	}


	static class Scan {

        /**
         * The basePackages to scan , the multiple-value is delimited by comma
         *
         * @see EnableDubbo#scanBasePackages()
         */
        private Set<String> basePackages = new LinkedHashSet<>();

        public Set<String> getBasePackages() {
            return basePackages;
        }

        public void setBasePackages(Set<String> basePackages) {
            this.basePackages = basePackages;
        }
    }
}
