package lhy.nrpc.core.route.impl;

import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lhy.nrpc.core.route.RpcLoadBalance;

/**
 * round
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午6:12:04   
 *
 */
public class RpcLoadBalanceRoundStrategy extends RpcLoadBalance {

    private ConcurrentMap<String, Integer> routeCountEachJob = new ConcurrentHashMap<String, Integer>();
    private long CACHE_VALID_TIME = 0;
    private int count(String serviceKey) {
        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            routeCountEachJob.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 24*60*60*1000;
        }

        // count++
        Integer count = routeCountEachJob.get(serviceKey);
        count = (count==null || count>1000000)?(new Random().nextInt(100)):++count;  // 初始化时主动Random一次，缓解首次压力
        routeCountEachJob.put(serviceKey, count);
        return count;
    }

    @Override
    public String route(String serviceKey, TreeSet<String> addressSet) {
        // arr
        String[] addressArr = addressSet.toArray(new String[addressSet.size()]);

        // round
        String finalAddress = addressArr[count(serviceKey)%addressArr.length];
        return finalAddress;
    }
    
    //注意这里有点问题，为了省事直接从下标为0的地址开始了。
	@Override
	public void resetServiceKey(String serviceKey, String serverAdress) {
		// TODO Auto-generated method stub
		
		routeCountEachJob.put(serviceKey, 0);
	}

}
