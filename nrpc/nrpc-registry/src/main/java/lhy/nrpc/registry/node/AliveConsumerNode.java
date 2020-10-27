package lhy.nrpc.registry.node;

import lhy.nrpc.common.reqres.http.node.BaseConsumerNode;

/**
 * 活跃的服务提供者的节点信息
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午12:52:30   
 *
 */
public class AliveConsumerNode extends BaseConsumerNode{
	
	
	/**   
	 * @Description:   
	 * @author: lhy 
	 * @date:   2020年8月7日 下午6:10:38   
	 *   
	 */
	private static final long serialVersionUID = 1L;
	
	private long lastHbTime;//最后一次心跳时间

	public long getLastHbTime() {
		return lastHbTime;
	}
	
	public void setLastHbTime(long lastHbTime) {
		this.lastHbTime = lastHbTime;
	}

	public AliveConsumerNode(String appid, String appname,String host,long lastHbTime) {
		super(appid,appname,host);
		this.lastHbTime = lastHbTime;
	}
	
	
	
	
}
