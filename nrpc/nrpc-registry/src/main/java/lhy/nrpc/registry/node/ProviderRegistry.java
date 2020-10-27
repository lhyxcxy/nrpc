package lhy.nrpc.registry.node;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lhy.nrpc.common.reqres.http.node.BaseProviderNode;
import lhy.nrpc.common.utils.DateUtil;
import lhy.nrpc.registry.config.RegistryConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 模拟注册中心,改为线程安全的！！！！！！！！！！！！！
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午1:01:17   
 *
 */
public class ProviderRegistry {
    private static Logger logger = LoggerFactory.getLogger(ProviderRegistry.class);

    private static Map<String,AliveProviderNode>serviceAndAddressesMap=new ConcurrentHashMap<>();

    
    public static Map<String, AliveProviderNode> getServiceAndAddressesMap() {
		return serviceAndAddressesMap;
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
    public static void register(BaseProviderNode providerNode) {
    	String name=providerNode.getAppname();
    	String host=providerNode.getHost();
    	Integer port=providerNode.getPort();
    	String appid=providerNode.getAppid();
    	Set<String>services= providerNode.getServiceIds();
    	StringBuilder sb=new StringBuilder("服务:");
    	sb.append(name).append(",注册的地址为：").append(host+":"+port).append("，包含的服务列表：").append(services.toString());
    	//logger.debug(sb.toString());
    	AliveProviderNode r=new AliveProviderNode(providerNode.getAppid(),name,host,port,services,DateUtil.getNowTime());
    	serviceAndAddressesMap.put(appid, r);
	}
    /**
     * 取消注册
     * @Description:   
     * @Creator: lhy
     * @CreateTime: 2020年8月11日 下午12:10:06
     * @Modifier: 
     * @ModifyTime:
     * @Reasons:
     * @param appid
     */
    public static void unRegister(String appid) {
    	serviceAndAddressesMap.remove(appid);
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
    public static Set<BaseProviderNode> getServiceServerAddresses(String serviceId){
    	Set<BaseProviderNode>nodes=new HashSet<>();
    	Iterator<Entry<String, AliveProviderNode>> it = serviceAndAddressesMap.entrySet().iterator();  
        while(it.hasNext()){
        	
            Entry<String, AliveProviderNode> entry = it.next();  
            AliveProviderNode node=entry.getValue();
            if((DateUtil.getNowTime()-node.getLastHbTime())/1000>=RegistryConfig.getRegister_heartbat_outtime())  
            {
            	it.remove();
            	continue;
            }
            if(node.getServiceIds().contains(serviceId))
            	nodes.add(entry.getValue()); 
            
        }  
    	return nodes;
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
    public static Set<BaseProviderNode> getAliveProviderNodes(Set<String> serviceIds){
    	Iterator<Entry<String, AliveProviderNode>> it = serviceAndAddressesMap.entrySet().iterator();
    	Set<BaseProviderNode> rlist=new LinkedHashSet<BaseProviderNode>();
        while(it.hasNext()){
        	
            Entry<String, AliveProviderNode> entry = it.next();  
            AliveProviderNode node=entry.getValue();
            if((DateUtil.getNowTime()-node.getLastHbTime())/1000>=RegistryConfig.getRegister_heartbat_outtime())  
            {
            	it.remove();
            	continue;
            }
            System.out.println("1");
            Set<String> tserviceIds= deepCopySet(serviceIds);
            tserviceIds.retainAll(node.getServiceIds());
            if(tserviceIds.size()>0)
            	rlist.add(new BaseProviderNode(node.getAppid(), node.getAppname(), node.getHost(),node.getPort(), tserviceIds));
            
        }  
    	return rlist;
    }
    

    private static Set<String> deepCopySet(Set<String> set){
    	Set<String>newSet=new HashSet<>();
    	for(String s:set)
    		newSet.add(s);
		return newSet;
    }
    /**
     * 检测provider超时，删除超时的注册节点
     * @Description:   
     * @Creator: lhy
     * @CreateTime: 2020年7月31日 下午1:39:21
     * @Modifier: 
     * @ModifyTime:
     * @Reasons:
     * @return 活跃的节点信息
     */
    public static Set<String> heartbeatCheck(){
    	Iterator<Entry<String, AliveProviderNode>> it = serviceAndAddressesMap.entrySet().iterator();  
        while(it.hasNext()){  
            Entry<String, AliveProviderNode> entry = it.next();  
            if((DateUtil.getNowTime()-entry.getValue().getLastHbTime())/1000>=RegistryConfig.getRegister_heartbat_outtime())  
                it.remove(); 
        }  
        return serviceAndAddressesMap.keySet();
    }
    

}
