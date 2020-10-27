package lhy.nrpc.common.reqres.http.node;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;



public class BaseNode implements Serializable{
	/**   
	 * @Description:   
	 * @author: lhy 
	 * @date:   2020年8月7日 下午6:35:14   
	 *   
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 应用id
	 */
	@NotEmpty
	private String appid;
	/**
	 * 应用名称
	 */
	@NotEmpty
	private String appname;
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public BaseNode(String appid, String appname) {
		super();
		this.appid = appid;
		this.appname = appname;
	}
	public BaseNode() {
		super();
	}
	
	
}
