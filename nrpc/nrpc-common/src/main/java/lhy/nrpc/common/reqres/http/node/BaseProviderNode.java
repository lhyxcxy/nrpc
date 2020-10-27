package lhy.nrpc.common.reqres.http.node;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

/**
 * 服务提供者节点
 * @Description:   
 * @author: lhy 
 * @date:   2020年8月7日 下午6:05:48   
 *
 */
public class BaseProviderNode extends BaseNode implements Serializable{

	/**   
	 * @Description:   
	 * @author: lhy 
	 * @date:   2020年8月7日 下午6:08:38   
	 *   
	 */
	private static final long serialVersionUID = 1L;


	@NotEmpty
	private String host;
	@NotEmpty
	private Integer port;

	/**
	 * 服务提供的services列表
	 */
	
	private Set<String>serviceIds;
	
	public Set<String> getServiceIds() {
		return serviceIds;
	}
	public void setServiceIds(Set<String> serviceIds) {
		this.serviceIds = serviceIds;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	
	public String getAddress() {
		return host+":"+port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public BaseProviderNode(String appid, String appname,String host,Integer port, Set<String> serviceIds) {
		super(appid,appname);
		this.host = host;
		this.port = port;
		this.serviceIds = serviceIds;
	}
	public BaseProviderNode(String appid, String appname) {
		super(appid, appname);
	}
	
	public BaseProviderNode(String appid) {
		super.setAppid(appid);;
	}
	public BaseProviderNode() {
		super();
	}
	
	
}
