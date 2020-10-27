package lhy.nrpc.common.reqres.http;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

public class BaseRequest  implements Serializable{
	/**   
	 * @Description:   
	 * @author: lhy 
	 * @date:   2020年8月7日 下午6:08:59   
	 *   
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 请求秘钥
	 */
	@NotEmpty
	private String accesstoken;

	
	public String getAccesstoken() {
		return accesstoken;
	}
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
	
	public BaseRequest() {
		super();
	}
	public BaseRequest(@NotEmpty String accesstoken) {
		super();
		this.accesstoken = accesstoken;
	}

}
