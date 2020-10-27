package lhy.nrpc.core.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lhy.nrpc.common.reqres.rpc.RpcRequest;
import lhy.nrpc.common.utils.ServiceBeanNameUtil;
import lhy.nrpc.core.utils.SpringContextUtil;

/**
 * RPC service 工厂
 * @Description:   未实现如下：
 * 					自己可通过扫描RpcService注解，获得所有的RPC的servece。
 * 				      如果使用spring 则直接通过context根据接口全限定名获得service即可，不需要自己初始化service。
 * 				      目前仅仅是根据接口全限定名获得唯一实现类的实例，但是考虑到一个接口可能有多个实现类，
 * 					或者多个版本，需要在RpcService注解中自己定义，并实现查找到service的逻辑。
 * @author: lhy 
 * @date:   2020年7月31日 上午10:36:39   
 *
 */
public class RpcServiceFactory {
	
	
	
	

	/**
	 * 获得service
	 * @Description:   
	 * @Creator: lhy
	 * @CreateTime: 2020年7月31日 上午9:47:45
	 * @Modifier: 
	 * @ModifyTime:
	 * @Reasons:
	 * @param infname
	 * @return
	 */
	public static Object getService(RpcRequest request){
		return SpringContextUtil.getBean(request.getServiceId());
		//return null;
	}
	
	/**
	 * 反射调用service的method
	 * @Description:   
	 * @Creator: lhy
	 * @CreateTime: 2020年7月31日 上午10:35:49
	 * @Modifier: 
	 * @ModifyTime:
	 * @Reasons:
	 * @param request
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	// invoke
	public static Object execServiceMethod(RpcRequest request) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
			Object serviceBean=getService(request);
			Class<?> serviceClass = serviceBean.getClass();
			String methodName = request.getMethodName();
			Class<?>[] parameterTypes = request.getParameterTypes();
			Object[] parameters = request.getParameters();
            Method method = serviceClass.getMethod(methodName, parameterTypes);
            method.setAccessible(true);
			Object result = method.invoke(serviceBean, parameters);
			return result;
	}
}
