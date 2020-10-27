package lhy.nrpc.common.constant;
/**
 * 全局常量
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午2:12:50   
 *
 */
public class RpcConstant {
	
	 public static final int MAX_LENGTH = 20 * 1024 * 1024;
	 public static final int BEAT_INTERVAL = 30;
	 
	 //服务提供者心跳超时时间，单位S
	 public static final int PROVIDER_HEARTBAT_OUTTIME = 10;
	 
	 public static final String RPC_REFERENCE_VERSION="version";
	 public static final String RPC_REFERENCE_GROUP="group";
	 public static final String RPC_REFERENCE_TIMEOUT="timeout";
	 public static final String RPC_REFERENCE_LOADBALANCE="loadbalance";
	 
	 public static final String REGISTRY_ACCESSTOKEN_NAME="accesstoken";
	 
	 public static final String REGISTRY_PROVIDER_REGISTER_URL="/remote/provider/register";
	 public static final String REGISTRY_PROVIDER_UNREGISTER_URL="/remote/provider/unRegister";
	 public static final String REGISTRY_CONSUMER_ALIVEPROVIDER_URL="/remote/provider/aliveProviders";
	 public static final String REGISTRY_CONSUMER_REGISTER_URL="/remote/consumer/register";
	 public static final String REGISTRY_CONSUMER_UNREGISTER_URL="/remote/consumer/unRegister";
	 
	 /**
	  * nrpc的springboot配置文件内容前缀
	  */
	 public static final String NRPC_CONFIG_PREFIX = "nrpc";
	 
}
