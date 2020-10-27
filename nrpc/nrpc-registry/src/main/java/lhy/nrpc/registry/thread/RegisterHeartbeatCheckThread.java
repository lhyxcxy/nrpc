package lhy.nrpc.registry.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lhy.nrpc.registry.node.ConsumerRegistry;
import lhy.nrpc.registry.node.ProviderRegistry;

/**
 * 注册心跳超时检测线程
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午1:48:31   
 *
 */
public class RegisterHeartbeatCheckThread {
	static Logger logger=LoggerFactory.getLogger(RegisterHeartbeatCheckThread.class);
	private static boolean stop=false;
	static Thread checkProviderThread=null;
	static Thread checkConsumerThread=null;
	public static void start(){
		
		checkProviderThread=new Thread(() -> {
			logger.info("checkProviderThread线程启动");
			while(stop){
				try {
					ProviderRegistry.heartbeatCheck();
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					
					if(stop)//如果是真关闭的
					{
						logger.error("checkProviderThread 关闭",e);
						break;
					}
					logger.error(e.getMessage(),e);
				}
			}
		});
		
		checkProviderThread.setDaemon(true);//设置为守护线程，同容器一块销毁
		checkProviderThread.setName("checkProviderThread超时心跳检测线程");
		checkProviderThread.start();
		
		
		checkConsumerThread=new Thread(() -> {
			logger.info("checkConsumerThread线程启动");
			while(stop){
				try {
					ConsumerRegistry.heartbeatCheck();
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					
					if(stop)//如果是真关闭的
					{
						logger.error("checkConsumerThread 关闭",e);
						break;
					}
					logger.error(e.getMessage(),e);
				}
			}
		});
		
		checkConsumerThread.setDaemon(true);//设置为守护线程，同容器一块销毁
		checkConsumerThread.setName("checkConsumerThread超时心跳检测线程");
		checkConsumerThread.start();
	}
	
	/**
	 * 线程关闭
	 * @Description:   
	 * @Creator: lhy
	 * @CreateTime: 2020年7月31日 下午2:51:59
	 * @Modifier: 
	 * @ModifyTime:
	 * @Reasons:
	 */
	public static void toStop(){
		stop=true;
		// interrupt and wait
		checkProviderThread.interrupt();
		checkConsumerThread.interrupt();
        try {
        	checkProviderThread.join();
        	checkConsumerThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
	}
}
