package lhy.nrpc.registry.node;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lhy.nrpc.common.reqres.http.node.BaseConsumerNode;
import lhy.nrpc.common.utils.DateUtil;
import lhy.nrpc.registry.config.RegistryConfig;

import java.util.Iterator;
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
public class ConsumerRegistry {
    private static Logger logger = LoggerFactory.getLogger(ConsumerRegistry.class);

    private static Map<String,AliveConsumerNode>aliveConsumerMap=new ConcurrentHashMap<>();
 
    
    public static Map<String, AliveConsumerNode> getAliveConsumerMap() {
		return aliveConsumerMap;
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
    public static void register(BaseConsumerNode consumerNode) {
    	String appname=consumerNode.getAppname();
    	String host=consumerNode.getHost();
    	String appid=consumerNode.getAppid();

    	StringBuilder sb=new StringBuilder("服务:");
    	sb.append(appname).append(",注册的地址为：").append(host);
    	//logger.debug(sb.toString());
    	AliveConsumerNode node=new AliveConsumerNode(appid, appname, host, DateUtil.getNowTime());
    	aliveConsumerMap.put(appid, node);
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
    	aliveConsumerMap.remove(appid);
	}
   
  
    /**
     * 检测consumer超时，删除超时的注册节点
     * @Description:   
     * @Creator: lhy
     * @CreateTime: 2020年7月31日 下午1:39:21
     * @Modifier: 
     * @ModifyTime:
     * @Reasons:
     * @return 活跃的节点信息
     */
    public static Set<String> heartbeatCheck(){
    	Iterator<Entry<String, AliveConsumerNode>> it = aliveConsumerMap.entrySet().iterator();  
        while(it.hasNext()){  
            Entry<String, AliveConsumerNode> entry = it.next();  
            if((DateUtil.getNowTime()-entry.getValue().getLastHbTime())/1000>=RegistryConfig.getRegister_heartbat_outtime())  
                it.remove(); 
        }  
        return aliveConsumerMap.keySet();
    }
    

}
