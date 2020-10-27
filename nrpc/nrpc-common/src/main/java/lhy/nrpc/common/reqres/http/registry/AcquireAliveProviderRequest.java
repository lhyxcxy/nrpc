package lhy.nrpc.common.reqres.http.registry;

import java.util.Set;

import lhy.nrpc.common.reqres.http.BaseRequest;

/**
 * 活跃的provider请求实体
 * @Description:   
 * @author: lhy 
 * @date:   2020年8月7日 下午4:39:03   
 *
 */
public class AcquireAliveProviderRequest extends BaseRequest{

	/**   
	 * @Description:   
	 * @author: lhy 
	 * @date:   2020年8月7日 下午6:09:10   
	 *   
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ServiceBean:{接口名}:{group}:{version}
	 * 参考lhy.nrpc.common.utils.ServiceBeanNameUtil
	 * 当前消费者需要的服务列表
	 */
	private Set<String> serviceIds;

	public Set<String> getServiceIds() {
		return serviceIds;
	}

	public void setServiceIds(Set<String> serviceIds) {
		this.serviceIds = serviceIds;
	}

	public AcquireAliveProviderRequest(String accesstoken,Set<String> serviceIds) {
		super(accesstoken);
		this.serviceIds = serviceIds;
	}

	public AcquireAliveProviderRequest() {
		super();
	}
	
	
	
	
}
