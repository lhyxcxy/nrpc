package lhy.nrpc.common.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 客户端根据RpcReference接口获得代理service
 * @Description:   
 * @author: lhy 
 * @date:   2020年8月3日 下午5:16:10   
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Inherited
public @interface NRpcReference {

    /**
     * Service version, default value is empty string
     */
    String version() default "";

    long timeout() default -1;
    String loadbalance() default "";
    /**
     * The id
     *
     * @return default value is empty
     * @since 2.7.3
     */
    String id() default "";
}