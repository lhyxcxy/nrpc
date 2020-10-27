package lhy.nrpc.core.config;

import java.util.UUID;

public class NRpcNodeConfig {
	/**
	 * 集群唯一id
	 */
	private String appid="nrpc-appid-"+UUID.randomUUID().toString();
	/**
	 * 集群节点名称
	 */
	private String appname="nrpc-appname-"+UUID.randomUUID().toString();;
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
	
	
}
