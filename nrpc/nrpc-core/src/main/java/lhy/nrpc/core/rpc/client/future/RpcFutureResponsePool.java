package lhy.nrpc.core.rpc.client.future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lhy.nrpc.common.reqres.rpc.RpcResponse;
import lhy.nrpc.core.config.ProviderConfig;

import java.util.concurrent.*;

/**
 * xxl-rpc invoker factory, init service-registry
 *
 * @author lhy
 */
public class RpcFutureResponsePool {
    private static Logger logger = LoggerFactory.getLogger(RpcFutureResponsePool.class);

    // ---------------------- default instance ----------------------

    private static volatile RpcFutureResponsePool instance = new RpcFutureResponsePool();

  //懒汉单例-双重验证
  	public static RpcFutureResponsePool getInstance() {
  		if (instance == null) {
  			synchronized (ProviderConfig.class) {
  				if (instance == null) 
  					instance = new RpcFutureResponsePool();
  			}
  		}
  		return instance;
  	}
  
    // ---------------------- future-response pool ----------------------

    // XxlRpcFutureResponseFactory

    private ConcurrentMap<String, RpcFutureResponse> futureResponsePool = new ConcurrentHashMap<String, RpcFutureResponse>();

    public void setInvokerFuture(String requestId, RpcFutureResponse futureResponse) {
        futureResponsePool.put(requestId, futureResponse);
    }

    public void removeInvokerFuture(String requestId) {
        futureResponsePool.remove(requestId);
    }

    public void notifyInvokerFuture(String requestId, final RpcResponse xxlRpcResponse) {

        // get
        final RpcFutureResponse futureResponse = futureResponsePool.get(requestId);
        if (futureResponse == null) {
            return;
        }

        // other nomal type
        futureResponse.setResponse(xxlRpcResponse);

        // do remove
        futureResponsePool.remove(requestId);

    }




}
