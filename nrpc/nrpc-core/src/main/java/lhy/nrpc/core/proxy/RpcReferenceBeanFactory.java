package lhy.nrpc.core.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import lhy.nrpc.common.exception.NRpcException;
import lhy.nrpc.common.reqres.rpc.RpcRequest;
import lhy.nrpc.core.factory.NclientFactory;
import lhy.nrpc.core.registry.RemoteProviderRegistry;
import lhy.nrpc.core.route.LoadBalance;
import lhy.nrpc.core.route.RpcLoadBalance;
import lhy.nrpc.core.rpc.client.Nclient;

import java.lang.reflect.Proxy;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 通过传入接口、返回接口的动态代理类
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午12:38:41   
 *
 */
public class RpcReferenceBeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(RpcReferenceBeanFactory.class);
    //private static final String DEFAULT_VERSION="0.0.1";//调用相同接口的不同版本
    public static final long DEFAULT_TIMEOUT = 5000;//超时时间
    private static final String DEFAULT_BALANCE = LoadBalance.RANDOM.toString();//超时时间


   



    // ---------------------- util ----------------------
    
    public  Object getRpcReferenceBean(String serviceId,Class<?> iface,String tbalance,long timeout) throws Exception {
    	String balance=StringUtils.isEmpty(tbalance)?DEFAULT_BALANCE:tbalance;
 
        // newProxyInstance
        return Proxy.newProxyInstance(Thread.currentThread()
                        .getContextClassLoader(), new Class[]{iface},
                (proxy, method, args) -> {

                    // method param
                    String className = method.getDeclaringClass().getName();    // iface.getName()
                    String methodName = method.getName();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object[] parameters = args;


                    //过滤不需要拦截的方法
                    // filter method like "Object.toString()"
                    if (className.equals(Object.class.getName())) {
                        logger.info(">>>>>>>>>>> nrpc proxy class-method not support [{}#{}]", className, methodName);
                        throw new NRpcException("nrpc proxy class-method not support");
                    }

                    // request
                    RpcRequest xxlRpcRequest = new RpcRequest();
                    xxlRpcRequest.setRequestId(UUID.randomUUID().toString());
                    xxlRpcRequest.setCreateMillisTime(System.currentTimeMillis());
                    xxlRpcRequest.setClassName(className);
                    xxlRpcRequest.setMethodName(methodName);
                    xxlRpcRequest.setParameterTypes(parameterTypes);
                    xxlRpcRequest.setParameters(parameters);
                    //xxlRpcRequest.setVersion(version);
                    //xxlRpcRequest.setGroup(group);
                    xxlRpcRequest.setServiceId(serviceId);
                    //这里可以做 负载均衡，熔断降级
                    // send
                   
                    		TreeSet<String> addressSet=RemoteProviderRegistry.getServiceServerAddresses(serviceId);
                    		String serverAddress="";
                    		RpcLoadBalance rpcLoadBalance=LoadBalance.match(balance).rpcInvokerRouter;
                    		
                    		while(addressSet.size()>0)
                    		{	
                    			try {
									
									serverAddress=rpcLoadBalance.route(serviceId, addressSet);
									Nclient client=NclientFactory.getClient(serverAddress);
									return client.send(xxlRpcRequest,timeout,TimeUnit.MILLISECONDS).getResult();
                    			} catch (Exception e) {
									logger.error(e.getMessage(),e);
									addressSet.remove(serverAddress);//删除失败的地址，再次调用负载均衡算法
									if(StringUtils.isEmpty(serverAddress))
										throw new NRpcException("server地址为空");
									//根据serviceKey重置负载均衡算法的初始值
									rpcLoadBalance.resetServiceKey(serviceId, serverAddress);
									
								}
                    			
                    		}
							if(addressSet.size()<=0)
								throw new NRpcException("请求服务端失败");
							return proxy;
                       

                });
    }
    
   


}


