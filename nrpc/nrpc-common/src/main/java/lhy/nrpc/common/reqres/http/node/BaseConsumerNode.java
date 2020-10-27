package lhy.nrpc.common.reqres.http.node;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

/**
 * 服务提供者节点
 * @Description:   
 * @author: lhy 
 * @date:   2020年8月7日 下午6:05:48   
 *
 */
public class BaseConsumerNode extends BaseNode implements Serializable{

	/**   
	 * @Description:   
	 * @author: lhy 
	 * @date:   2020年8月7日 下午6:08:38   
	 *   
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主机host
	 */
	@NotEmpty
	private String host;

	
	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}



	public BaseConsumerNode(String appid, String appname,String host) {
		super(appid, appname);
		this.host=host;
	}
	public BaseConsumerNode(String appid) {
		super.setAppid(appid);
	}


	public BaseConsumerNode() {
		super();
	}
	
	
}
