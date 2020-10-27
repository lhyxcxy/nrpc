package lhy.nrpc.core.registry;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lhy.nrpc.common.constant.RpcConstant;
import lhy.nrpc.common.exception.NRpcException;
import lhy.nrpc.common.reqres.http.BaseResponse;
import lhy.nrpc.common.reqres.http.node.BaseProviderNode;
import lhy.nrpc.common.reqres.http.registry.AcquireAliveProviderRequest;
import lhy.nrpc.common.reqres.http.registry.RegisterProviderRequest;
import lhy.nrpc.common.utils.HttpUtil;
import lhy.nrpc.common.utils.JSONUtil;
import lhy.nrpc.core.config.NRpcNodeConfig;
import lhy.nrpc.core.config.ProviderConfig;
import lhy.nrpc.core.config.RegistryConfig;
import lhy.nrpc.core.proxy.ReferenceAnnotationBeanPostProcessor;
import lhy.nrpc.core.proxy.s.ServiceAnnotationBeanPostProcessor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.*;
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
public class RemoteProviderRegistry {
    private static Logger logger = LoggerFactory.getLogger(RemoteProviderRegistry.class);
    static ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
    static WriteLock writeLock=lock.writeLock();
    static ReadLock readLock=lock.readLock();
    
    private RegistryConfig registryConfig;
    
    private ProviderConfig providerConfig;
    
    private NRpcNodeConfig nrRpcNodeConfig;
    
    

    public RemoteProviderRegistry(RegistryConfig registryConfig, ProviderConfig providerConfig,NRpcNodeConfig nrRpcNodeConfig) {
		super();
		this.registryConfig = registryConfig;
		this.providerConfig = providerConfig;
		this.nrRpcNodeConfig=nrRpcNodeConfig;
	}



	/**
     * key=appid
     */
    private static Map<String,BaseProviderNode>aliveProviderNodeMap=new ConcurrentHashMap<>();
   /* *//**
     * key=serviceId
     *//*
    private static Map<String,String>serviceIdAndaliveProviderAddressMap=new ConcurrentHashMap<>();*/
    
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
    	BaseProviderNode node=new BaseProviderNode(nrRpcNodeConfig.getAppid(), nrRpcNodeConfig.getAppname()
    			, providerConfig.getHost(), providerConfig.getPort(), ServiceAnnotationBeanPostProcessor.getServiceBeanIds());
    	HttpUtil.post(registryConfig.getRequestDetailAddress(RpcConstant.REGISTRY_PROVIDER_REGISTER_URL), 
    			JSONUtil.bean2Json(new RegisterProviderRequest(registryConfig.getAccesstoken(),node)));
    	StringBuilder sb=new StringBuilder("服务:");
    	sb.append(node.getAppname()).append(",注册的地址为：").append(node.getAddress()).append("，包含的服务列表：").append(node.getServiceIds().toString());
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
    	aliveProviderNodeMap.remove(appid);
    	HttpUtil.post(registryConfig.getRequestDetailAddress(RpcConstant.REGISTRY_PROVIDER_UNREGISTER_URL),
    			JSONUtil.bean2Json(new RegisterProviderRequest(registryConfig.getAccesstoken(), new BaseProviderNode(appid))));
	}
    /*public static void main(String[] args) {
    	BaseProviderNode node=new BaseProviderNode("nrRpcNodeConfig.getAppid()", "nrRpcNodeConfig.getAppname()"
    			, "127.0.0.1", 18031, ServiceAnnotationBeanPostProcessor.getServiceBeanIds());
    	Set<String>a=new HashSet<>();
    	a.add("ServiceBean:lhy.nrpc.test.api.TestService::");
    	String aaa=HttpUtil.post("http://127.0.0.1:18330/nrpc-registry/"+RpcConstant.REGISTRY_CONSUMER_ALIVEPROVIDER_URL, 
    			JSONUtil.bean2Json(new AcquireAliveProviderRequest("a123", a)));
    	BaseResponse aa=JSONUtil.json2Bean(aaa, new BaseResponse<List<BaseProviderNode>>().getClass());
    	System.out.println(aa);
	}*/
    /**
     * 本地@reference对应的serviceIds,系统初始化或者客户端多次请求失败的时候
     * @Description:   
     * @Creator: lhy
     * @CreateTime: 2020年8月21日 下午1:52:34
     * @Modifier: 
     * @ModifyTime:
     * @Reasons:
     * @param serviceIds
     */
    private  void resetAboutLocalRferenceAliveProviders(Set<String>serviceIds) {
    	String r=HttpUtil.post(registryConfig.getRequestDetailAddress(RpcConstant.REGISTRY_CONSUMER_ALIVEPROVIDER_URL), 
    			JSONUtil.bean2Json(new AcquireAliveProviderRequest(registryConfig.getAccesstoken(), serviceIds)));
    	BaseResponse<?> bs=HttpUtil.toResponse(r);
		@SuppressWarnings("unchecked")
		List<Object> set=(List<Object>)bs.getData();
    	//使用写锁，保证clear和put的原子性，保证在clear的时候，无法查询到活跃列表，否则的话会出现列表为空的现象
    	if(set.size()<=0)
    		throw new NRpcException("没有服务端提供服务");
    	try {
    	writeLock.lock();
    	aliveProviderNodeMap.clear();
	    	for(Object m:set)
	    	{
	    		BaseProviderNode node=JSONUtil.json2Bean(JSONUtil.bean2Json(m),BaseProviderNode.class);
	    		aliveProviderNodeMap.put(node.getAppid(), node);
	    	}
    	} catch (Exception e) {
			// TODO: handle exception
    		throw new NRpcException(e.getMessage(),e);
		}finally {
			writeLock.unlock();
		}
	}
    /**
     * 本地@reference对应的serviceIds,系统初始化或者客户端多次请求失败的时候
     * @Description:   
     * @Creator: lhy
     * @CreateTime: 2020年8月23日 下午2:32:35
     * @Modifier: 
     * @ModifyTime:
     * @Reasons:
     */
    public  void  resetAboutLocalRferenceAliveProviders(){
    	Set<String>serviceIds= ReferenceAnnotationBeanPostProcessor.getNeedServiceIds();
    	if(serviceIds.size()>0)
    		resetAboutLocalRferenceAliveProviders(serviceIds);
    }

   
    
    /**
     * 获得包含service的活跃的server列表。
     * @Description:   
     * @Creator: lhy
     * @CreateTime: 2020年7月31日 下午1:02:41
     * @Modifier: 
     * @ModifyTime:
     * @Reasons:
     * @param serviceId
     */
    public  static TreeSet<String> getServiceServerAddresses(String serviceId){
    	TreeSet<String>raddresses=new TreeSet<>();
    	//使用读锁，保证在写时会阻塞，同时可以满足并发读
    	try {
    	readLock.lock();
    	Iterator<Entry<String, BaseProviderNode>> it = aliveProviderNodeMap.entrySet().iterator();  
        while(it.hasNext()){  
            Entry<String, BaseProviderNode> entry = it.next();  
            if(entry.getValue().getServiceIds().contains(serviceId))
            	raddresses.add(entry.getValue().getAddress()); 
        } 
    	} catch (Exception e) {
			throw new NRpcException(e.getMessage(),e);
		}finally {
			readLock.unlock();
		}
        
    	return raddresses;
    }
    
   
    
}
