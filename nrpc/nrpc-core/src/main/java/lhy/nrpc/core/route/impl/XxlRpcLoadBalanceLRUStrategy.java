package lhy.nrpc.core.route.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lhy.nrpc.core.route.RpcLoadBalance;

/**
 * lru
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午6:11:31   
 *
 */
public class XxlRpcLoadBalanceLRUStrategy extends RpcLoadBalance {
Logger logger=LoggerFactory.getLogger(XxlRpcLoadBalanceLRUStrategy.class);
    private ConcurrentMap<String, LinkedHashMap<String, String>> jobLRUMap = new ConcurrentHashMap<String, LinkedHashMap<String, String>>();
    private long CACHE_VALID_TIME = 0;

    public String doRoute(String serviceKey, TreeSet<String> addressSet) {

        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            jobLRUMap.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 1000*60*60*24;
        }

        // init lru
        LinkedHashMap<String, String> lruItem = jobLRUMap.get(serviceKey);
        if (lruItem == null) {
            /**
             * LinkedHashMap
             *      a、accessOrder：ture=访问顺序排序（get/put时排序）/ACCESS-LAST；false=插入顺序排期/FIFO；
             *      b、removeEldestEntry：新增元素时将会调用，返回true时会删除最老元素；可封装LinkedHashMap并重写该方法，比如定义最大容量，超出是返回true即可实现固定长度的LRU算法；
             */
            lruItem = new LinkedHashMap<String, String>(16, 0.75f, true){
                /**   
				 * @Description:   
				 * @author: lhy 
				 * @date:   2020年7月31日 上午11:27:44   
				 *   
				 */
				private static final long serialVersionUID = 1L;

				@Override
                protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                    if(super.size() > 1000){
                        return true;
                    }else{
                        return false;
                    }
                }
            };
            jobLRUMap.putIfAbsent(serviceKey, lruItem);
        }

        // put new
        for (String address: addressSet) {
            if (!lruItem.containsKey(address)) {
                lruItem.put(address, address);
            }
        }
        // remove old
        List<String> delKeys = new ArrayList<>();
        for (String existKey: lruItem.keySet()) {
            if (!addressSet.contains(existKey)) {
                delKeys.add(existKey);
            }
        }
        if (delKeys.size() > 0) {
            for (String delKey: delKeys) {
                lruItem.remove(delKey);
            }
        }

        // load
        String eldestKey = lruItem.entrySet().iterator().next().getKey();
        String eldestValue = lruItem.get(eldestKey);
        return eldestValue;
    }

    @Override
    public String route(String serviceKey, TreeSet<String> addressSet) {
        String finalAddress = doRoute(serviceKey, addressSet);
        return finalAddress;
    }

	@Override
	public void resetServiceKey(String serviceKey, String serverAdress) {
		// TODO Auto-generated method stub
		try {
			
		
		jobLRUMap.get(jobLRUMap).remove(serverAdress);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}



}
