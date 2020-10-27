package lhy.nrpc.core.factory;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lhy.nrpc.common.exception.NRpcException;
import lhy.nrpc.common.serializer.HessianSerializer;
import lhy.nrpc.core.rpc.client.Nclient;

/**
 * client管理工厂
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午5:41:16   
 *
 */
public class NclientFactory {
	private static Map<String,Nclient>clientPool=new ConcurrentHashMap<String, Nclient>();
	
	private  static synchronized Nclient addClient(String serverAddress) throws MalformedURLException, InterruptedException {
	
		Nclient client=new Nclient(serverAddress, new HessianSerializer());
		client.init();
		clientPool.put(serverAddress, client);
		return client;
	}
	
	public static void remove(String serverAddress){
		clientPool.remove(serverAddress);
	}
	public static Nclient getClient(String serverAddress) throws NRpcException{
		Nclient client=clientPool.get(serverAddress);
		if(client==null)
		{
			try {
				
			
			return addClient(serverAddress);
			} catch (Exception e) {
				throw new NRpcException(e.getMessage(), e);
			}
		}
		return client;
	}
	

	
}
