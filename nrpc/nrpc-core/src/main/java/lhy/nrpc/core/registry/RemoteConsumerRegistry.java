package lhy.nrpc.core.registry;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lhy.nrpc.common.constant.RpcConstant;
import lhy.nrpc.common.reqres.http.node.BaseConsumerNode;
import lhy.nrpc.common.reqres.http.registry.RegisterConsumerRequest;
import lhy.nrpc.common.utils.HttpUtil;
import lhy.nrpc.common.utils.JSONUtil;
import lhy.nrpc.core.config.ConsumerConfig;
import lhy.nrpc.core.config.NRpcNodeConfig;
import lhy.nrpc.core.config.RegistryConfig;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * 模拟注册中心
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午1:01:17   
 *
 */
public class RemoteConsumerRegistry {
    private static Logger logger = LoggerFactory.getLogger(RemoteConsumerRegistry.class);
    static ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
    static WriteLock writeLock=lock.writeLock();
    static ReadLock readLock=lock.readLock();
    private RegistryConfig registryConfig;
    private ConsumerConfig consumerConfig;
    private NRpcNodeConfig nrRpcNodeConfig;
    
    public RemoteConsumerRegistry(RegistryConfig registryConfig, ConsumerConfig consumerConfig,NRpcNodeConfig nrRpcNodeConfig) {
		super();
		this.registryConfig = registryConfig;
		this.consumerConfig = consumerConfig;
		this.nrRpcNodeConfig=nrRpcNodeConfig;
	}

	/**
     * server注册
     * @Description:   
     * @Creator: lhy
     * @CreateTime: 2020年7月31日 下午1:00:55
     * @Modifier: 
     * @ModifyTime:
     * @Reasons:
     * @param address
     * @param services service 全限定name的集合
     */
    public  void register() {
    	BaseConsumerNode node=new BaseConsumerNode(nrRpcNodeConfig.getAppid(), nrRpcNodeConfig.getAppname(), consumerConfig.getHost());
    	HttpUtil.post(registryConfig.getRequestDetailAddress(RpcConstant.REGISTRY_CONSUMER_REGISTER_URL), 
    			JSONUtil.bean2Json(new RegisterConsumerRequest(registryConfig.getAccesstoken(),node)));
    	StringBuilder sb=new StringBuilder("服务:");
    	sb.append(node.getAppname()).append(",注册的地址为：").append(node.getHost());
    	//logger.info(sb.toString());
	}
    
    /**
     * 取消server节点注册，一般在节点destory时被调用。
     * @Description:   
     * @Creator: lhy
     * @CreateTime: 2020年7月31日 下午12:59:02
     * @Modifier: 
     * @ModifyTime:
     * @Reasons:
     * @param address
     */
    public  void unRegister() {
    	String appid=nrRpcNodeConfig.getAppid();
    	
    	HttpUtil.post(registryConfig.getRequestDetailAddress(RpcConstant.REGISTRY_CONSUMER_UNREGISTER_URL),
    			JSONUtil.bean2Json(new RegisterConsumerRequest(registryConfig.getAccesstoken(), new BaseConsumerNode(appid))));
    	StringBuilder sb=new StringBuilder("服务:");
    	sb.append(nrRpcNodeConfig.getAppname()).append(",从注册中心注销");
    	//logger.info(sb.toString());
	}
    
   
   
    
}
