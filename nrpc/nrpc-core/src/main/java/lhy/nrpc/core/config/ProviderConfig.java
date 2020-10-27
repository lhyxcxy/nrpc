package lhy.nrpc.core.config;

import lhy.nrpc.common.utils.HostNetUtil;

/**
 * 服务提供者配置
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午2:58:18   
 *
 */
public class ProviderConfig extends AbstractConfig{
	/**   
	 * @Description:   
	 * @author: lhy 
	 * @date:   2020年8月3日 下午3:47:36   
	 *   
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * server可能会存在虚拟网卡的问题，可以手工指定
	 */
	private String host=HostNetUtil.getRealIP();
	/**
	 * 端口
	 */
	private Integer port;
	
	/**
	 * io线程数量，绑定channel的线程
	 */
	private Integer ioworkerthreads=20;
	/**
     * 业务处理核心线程数
     */
    private Integer svcorethreads=10;

    /**
     * 业务处理最大线程数
     */
    private Integer svmaxthreads=100;
    /**
     * 业务线程队列长度
     */
    private Integer svthreadqueues=100;
    
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}


	

	

	public Integer getIoworkerthreads() {
		return ioworkerthreads;
	}

	public void setIoworkerthreads(Integer ioworkerthreads) {
		this.ioworkerthreads = ioworkerthreads;
	}

	public Integer getSvcorethreads() {
		return svcorethreads;
	}

	public void setSvcorethreads(Integer svcorethreads) {
		this.svcorethreads = svcorethreads;
	}

	public Integer getSvmaxthreads() {
		return svmaxthreads;
	}

	public void setSvmaxthreads(Integer svmaxthreads) {
		this.svmaxthreads = svmaxthreads;
	}

	public Integer getSvthreadqueues() {
		return svthreadqueues;
	}

	public void setSvthreadqueues(Integer svthreadqueues) {
		this.svthreadqueues = svthreadqueues;
	}

	

	
	

}
