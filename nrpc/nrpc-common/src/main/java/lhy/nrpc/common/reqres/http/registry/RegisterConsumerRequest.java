package lhy.nrpc.common.reqres.http.registry;

import javax.validation.constraints.NotEmpty;

import lhy.nrpc.common.reqres.http.BaseRequest;
import lhy.nrpc.common.reqres.http.node.BaseConsumerNode;

/**
 * 注册/取消注册consumer请求实体
 * @Description:   
 * @author: lhy 
 * @date:   2020年8月7日 下午4:39:56   
 *
 */
public class RegisterConsumerRequest extends BaseRequest{
	


	/**   
	 * @Description:   
	 * @author: lhy 
	 * @date:   2020年8月7日 下午6:09:03   
	 *   
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * consumer节点信息
	 */
	private BaseConsumerNode consumerrNode;

	public BaseConsumerNode getConsumerrNode() {
		return consumerrNode;
	}

	public void setConsumerrNode(BaseConsumerNode consumerrNode) {
		this.consumerrNode = consumerrNode;
	}

	public RegisterConsumerRequest(@NotEmpty String accesstoken, BaseConsumerNode consumerrNode) {
		super(accesstoken);
		this.consumerrNode = consumerrNode;
	}

	public RegisterConsumerRequest() {
		super();
	}

	
	
	
	
}
