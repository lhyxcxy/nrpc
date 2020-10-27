package lhy.nrpc.log.opt;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 操作日志切面
 * @Description:   
 * @author: lhy 
 * @date:   2019年7月10日 下午12:49:53   
 *
 */
@Aspect
@Component
public class NRpcOptLogAspect{
	private final static Logger logger = (Logger) LoggerFactory.getLogger(NRpcOptLogAspect.class);
	

	
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
    public void afterReturning(JoinPoint joinPoint,NRpcOptLog NRpcOptLog) throws Exception {
	       
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
	public void afterThrowing(JoinPoint joinPoint,NRpcOptLog NRpcOptLog,Throwable e) throws Exception {
		logger.info("###############"+NRpcOptLog.module()+""+NRpcOptLog.operate());

	}
	
	
	@Pointcut("@annotation(lhy.nrpc.log.opt.NRpcOptLog)")
	private void optPointCut(){

	}
	
	
	/**  
     *  
     *  
     * @param joinPoint 切点  
     * @return 方法描述  
     * @throws Exception  
     */    
	public Map<String, String> getMthodDescription(JoinPoint joinPoint)
			throws Exception {
		
		/*String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();*/
		
		return null;
	}
	
	

}
