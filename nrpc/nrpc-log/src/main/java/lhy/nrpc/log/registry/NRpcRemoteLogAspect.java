package lhy.nrpc.log.registry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import lhy.nrpc.common.constant.RpcConstant;


/**
 * 操作日志切面
 * @Description:   
 * @author: lhy 
 * @date:   2019年7月10日 下午12:49:53   
 *
 */
@Aspect
@Component
public class NRpcRemoteLogAspect{
	private final static Logger logger = (Logger) LoggerFactory.getLogger(NRpcRemoteLogAspect.class);
	

	
	/**
	 * 正常返回通知
	 * @Description:   
	 * @Creator: jyw
	 * @CreateTime: 2019-6-13 上午10:52:01
	 * @Modifier: 
	 * @ModifyTime:
	 * @Reasons:
	 * @param joinPoint
	 * @throws Exception 
	 */
	@AfterReturning(pointcut = "optPointCut()")
    public void afterReturning(JoinPoint joinPoint,NRpcRemoteLog NRpcOptLog) throws Exception {
		getMthodParameterNames(joinPoint).get(RpcConstant.REGISTRY_ACCESSTOKEN_NAME);
        logger.info("###############"+NRpcOptLog.module()+""+NRpcOptLog.operate());

	}
	/**
	 * 异常返回通知
	 * @Description:   
	 * @Creator: jyw
	 * @CreateTime: 2019-6-13 上午10:52:42
	 * @Modifier: 
	 * @ModifyTime:
	 * @Reasons:
	 * @param joinPoint
	 * @throws Exception 
	 */
	@AfterThrowing(pointcut = "optPointCut()", throwing = "e")
	public void afterThrowing(JoinPoint joinPoint,NRpcRemoteLog NRpcOptLog,Throwable e) throws Exception {
		logger.info("###############"+NRpcOptLog.module()+""+NRpcOptLog.operate());

	}
	
	
	@Pointcut("@annotation(lhy.nrpc.log.registry.NRpcOptLog)")
	private void optPointCut(){

	}
	
	
	/**
	 * 方法中的参数
	 * @Description:   
	 * @Creator: lhy
	 * @CreateTime: 2020年8月11日 上午10:53:54
	 * @Modifier: 
	 * @ModifyTime:
	 * @Reasons:
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMthodParameterNames(JoinPoint joinPoint)
			throws Exception {
		
		String classType = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        // 参数值
        Object[] args = joinPoint.getArgs();
        Class<?>[] classes = new Class[args.length];
        for (int k = 0; k < args.length; k++) {
            if (!args[k].getClass().isPrimitive()) {
                // 获取的是封装类型而不是基础类型
                String result = args[k].getClass().getName();
                @SuppressWarnings("rawtypes")
				Class s = map.get(result);
                classes[k] = s == null ? args[k].getClass() : s;
            }
        }
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        // 获取指定的方法，第二个参数可以不传，但是为了防止有重载的现象，还是需要传入参数的类型
        Method method = Class.forName(classType).getMethod(methodName, classes);
        // 参数名
        String[] parameterNames = pnd.getParameterNames(method);
        // 通过map封装参数和参数值
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        for (int i = 0; i < parameterNames.length; i++) {
            paramMap.put(parameterNames[i], args[i]);
        }
        return paramMap;
	}
	 private static HashMap<String, Class> map = new HashMap<String, Class>() {
	        /**   
		 * @Description:   
		 * @author: lhy 
		 * @date:   2020年8月11日 上午10:52:37   
		 *   
		 */
		private static final long serialVersionUID = 1L;

			{
	            put("java.lang.Integer", int.class);
	            put("java.lang.Double", double.class);
	            put("java.lang.Float", float.class);
	            put("java.lang.Long", long.class);
	            put("java.lang.Short", short.class);
	            put("java.lang.Boolean", boolean.class);
	            put("java.lang.Char", char.class);
	        }
	    };
	

}
