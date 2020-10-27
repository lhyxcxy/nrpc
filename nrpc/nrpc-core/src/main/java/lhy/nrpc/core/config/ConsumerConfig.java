package lhy.nrpc.core.config;

import lhy.nrpc.common.utils.HostNetUtil;

/**
 * 客户端配置
 * @Description:   
 * @author: lhy 
 * @date:   2020年8月3日 下午3:45:51   
 *
 */
public class ConsumerConfig extends AbstractConfig{ 
	
	/**   
	 * @Description:   
	 * @author: lhy 
	 * @date:   2020年8月3日 下午3:47:25   
	 *   
	 */
	private static final long serialVersionUID = 1L;
	
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
	
	public String getHost() {
		return HostNetUtil.getRealIP();
	}
    
    
}
