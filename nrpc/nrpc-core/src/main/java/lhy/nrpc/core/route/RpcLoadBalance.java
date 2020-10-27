package lhy.nrpc.core.route;

import java.util.TreeSet;

/**
 * 分组下机器地址相同，不同JOB均匀散列在不同机器上，保证分组下机器分配JOB平均；且每个JOB固定调度其中一台机器；
 *      a、virtual node：解决不均衡问题
 *      b、hash method replace hashCode：String的hashCode可能重复，需要进一步扩大hashCode的取值范围
 *
 * @author lhy 
 */
public abstract class RpcLoadBalance {

    public abstract String route(String serviceKey, TreeSet<String> addressSet);
   /**
    * 当service请求出错时，重置此service的初始值
    * @Description:   
    * @Creator: lhy
    * @CreateTime: 2020年7月31日 下午6:26:23
    * @Modifier: 
    * @ModifyTime:
    * @Reasons:
    * @param serviceKey
    * @param serverAdress
    */
    public abstract void resetServiceKey(String serviceKey,String serverAdress);
    
}
