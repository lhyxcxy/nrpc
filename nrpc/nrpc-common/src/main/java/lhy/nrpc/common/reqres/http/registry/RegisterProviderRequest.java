package lhy.nrpc.common.reqres.http.registry;

import javax.validation.constraints.NotEmpty;

import lhy.nrpc.common.reqres.http.BaseRequest;
import lhy.nrpc.common.reqres.http.node.BaseProviderNode;

/**
 * 注册/取消注册provider请求实体
 * @Description:   
 * @author: lhy 
 * @date:   2020年8月7日 下午4:39:56   
 *
 */
public class RegisterProviderRequest extends BaseRequest{
	
	

	/**   
	 * @Description:   
	 * @author: lhy 
	 * @date:   2020年8月7日 下午6:09:03   
	 *   
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * provider节点信息
	 */
	private BaseProviderNode providerNode;

	public BaseProviderNode getProviderNode() {
		return providerNode;
	}

	public void setProviderNode(BaseProviderNode providerNode) {
		this.providerNode = providerNode;
	}
	
	public RegisterProviderRequest(@NotEmpty String accesstoken, BaseProviderNode providerNode) {
		super(accesstoken);
		this.providerNode = providerNode;
	}

	public RegisterProviderRequest() {
		super();
	}

	
	
}
