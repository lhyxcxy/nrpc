package lhy.nrpc.log.registry;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Nrpc操作日志
 * @Description:   
 * @author: lhy 
 * @date:   2020年8月11日 上午10:17:48   
 *
 */
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NRpcRemoteLog {

	/**
	 * 模块
	 * @return
	 */
	String module()  default "";
	
	
	/**
	 * 操作
	 * @return
	 */
	String operate()  default "";
}
