package lhy.nrpc.core.config;

import java.util.Set;

import lhy.nrpc.common.exception.NRpcException;

/**
 * 协议配置
 * @Description:   
 * @author: lhy 
 * @date:   2020年8月3日 下午3:46:05   
 *
 */
public class RegistryConfig extends AbstractConfig{

	/**   
	 * @Description:   
	 * @author: lhy 
	 * @date:   2020年8月3日 下午3:47:39   
	 *   
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 注册中心地址,多个yoga英文逗号隔开
	 */
	private Set<String> addresses;
	/**
	 * 
	 */
	private  String accesstoken="";


	
	public  String getAccesstoken() {
		return accesstoken;
	}

	public  void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
	/**
	 * 根据算法获取注册中心地址，目前仅仅返回第一个注册中心地址
	 * @Description:   
	 * @Creator: lhy
	 * @CreateTime: 2020年8月11日 下午1:00:57
	 * @Modifier: 
	 * @ModifyTime:
	 * @Reasons:
	 * @return
	 */
	public String getAddress() {
		for(String ad:addresses)
			return ad;
		throw new NRpcException("注册中心地址不存在");
	}
	
	




	
	public Set<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<String> addresses) {
		this.addresses = addresses;
	}

	/**
	 * 获得详细请求地址
	 * @Description:   
	 * @Creator: lhy
	 * @CreateTime: 2020年8月11日 下午1:02:15
	 * @Modifier: 
	 * @ModifyTime:
	 * @Reasons:
	 * @param url
	 * @return
	 */
	public String getRequestDetailAddress(String url)
	{
		return getAddress()+"/"+url;
		
	}
	

}
