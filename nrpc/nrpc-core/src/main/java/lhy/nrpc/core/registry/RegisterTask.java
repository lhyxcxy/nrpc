package lhy.nrpc.core.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * netty server 启动后，注册自己到注册中心，每2秒刷新一次。
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午2:22:59   
 *
 */

public class RegisterTask {
	
	static Logger logger=LoggerFactory.getLogger(RegisterTask.class);
	private RemoteProviderRegistry providerRegistry;
	private RemoteConsumerRegistry consumerRegistry;
	
	
	public RegisterTask(RemoteProviderRegistry providerRegistry, RemoteConsumerRegistry consumerRegistry) {
		super();
		this.providerRegistry = providerRegistry;
		this.consumerRegistry = consumerRegistry;
	}

	
	private static boolean stop=false;
	static Thread providerRegisterThread=null;
	static Thread consumerRegisterThread=null;
	/**
	 * 启动线程
	 * @Description:   
	 * @Creator: lhy
	 * @CreateTime: 2020年7月31日 下午3:39:00
	 * @Modifier: 
	 * @ModifyTime:
	 * @Reasons:
	 */
	public void start(){
		providerRegisterThread=new Thread(() -> {
			logger.info("providerRegisterThread 线程启动");
			while(!stop){
				try {
					providerRegistry.register();
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					
					if(stop)//如果是真关闭的
					{
						logger.error("providerRegisterThread 关闭",e);
						break;
					}
					logger.error(e.getMessage(),e);
				}
			}
		});
		
		providerRegisterThread.setDaemon(true);//设置为守护线程，同容器一块销毁
		providerRegisterThread.setName("providerRegisterThread 定时刷新 注册中心的 注册的心跳时间 线程");
		providerRegisterThread.start();
		
		consumerRegisterThread=new Thread(() -> {
			logger.info("consumerRegisterThread 线程启动");
			while(!stop){
				try {
					consumerRegistry.register();
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					
					if(stop)//如果是真关闭的
					{
						logger.error("consumerRegisterThread 关闭",e);
						break;
					}
					logger.error(e.getMessage(),e);
				}
			}
		});
		
		consumerRegisterThread.setDaemon(true);//设置为守护线程，同容器一块销毁
		consumerRegisterThread.setName("consumerRegisterThread 定时刷新 注册中心的 注册的心跳时间 线程");
		consumerRegisterThread.start();
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
		providerRegisterThread.interrupt();
		consumerRegisterThread.interrupt();
        try {
        	providerRegisterThread.join();
        	consumerRegisterThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
	}
}
