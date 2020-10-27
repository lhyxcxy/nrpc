package lhy.nrpc.core.route;

import lhy.nrpc.core.route.impl.RpcLoadBalanceRoundStrategy;
import lhy.nrpc.core.route.impl.XxlRpcLoadBalanceConsistentHashStrategy;
import lhy.nrpc.core.route.impl.XxlRpcLoadBalanceLFUStrategy;
import lhy.nrpc.core.route.impl.XxlRpcLoadBalanceLRUStrategy;
import lhy.nrpc.core.route.impl.XxlRpcLoadBalanceRandomStrategy;

/**
 * 负载均衡枚举类
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午5:26:42   
 *
 */
public enum LoadBalance {

    RANDOM(new XxlRpcLoadBalanceRandomStrategy()),
    ROUND(new RpcLoadBalanceRoundStrategy()),
    LRU(new XxlRpcLoadBalanceLRUStrategy()),
    LFU(new XxlRpcLoadBalanceLFUStrategy()),
    CONSISTENT_HASH(new XxlRpcLoadBalanceConsistentHashStrategy());


    public final RpcLoadBalance rpcInvokerRouter;

    private LoadBalance(RpcLoadBalance rpcInvokerRouter) {
        this.rpcInvokerRouter = rpcInvokerRouter;
    }


    public static LoadBalance match(String name) {
        for (LoadBalance item : LoadBalance.values()) {
            if (item.equals(name)) {
                return item;
            }
        }
        return RANDOM;
    }
    


}