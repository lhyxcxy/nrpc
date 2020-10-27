package lhy.nrpc.core.rpc.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import lhy.nrpc.common.constant.RpcConstant;
import lhy.nrpc.common.serializer.Serializer;
import lhy.nrpc.common.utils.ThreadPoolUtil;
import lhy.nrpc.core.config.ProviderConfig;
import lhy.nrpc.core.rpc.server.handler.Nserverhandler;

/**
 * netty server
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午2:16:04   
 *
 */
public class Nserver {
	Logger logger=LoggerFactory.getLogger(Nserver.class);
	private  ProviderConfig providerConfig;
	private Serializer serializer;
	public Nserver(ProviderConfig providerConfig,Serializer serializer) {
		super();
		this.providerConfig=providerConfig;
		this.serializer=serializer;
	}

	private Thread thread;
	public void start() throws InterruptedException {
		thread = new Thread(() -> {
		// param
        final ThreadPoolExecutor serverHandlerPool = ThreadPoolUtil.makeThreadPool(
                "serverHandlerPool",
                providerConfig.getSvcorethreads(),
                providerConfig.getSvmaxthreads(),
                providerConfig.getSvthreadqueues()
                );
        
		ServerBootstrap b=new ServerBootstrap();
		EventLoopGroup bossGroup=new NioEventLoopGroup();
		EventLoopGroup workGroup=new NioEventLoopGroup(providerConfig.getIoworkerthreads());
		try {
		
		b.group(bossGroup,workGroup)
		.localAddress(new InetSocketAddress(providerConfig.getPort()))
		.channel(NioServerSocketChannel.class)//使用指定的NIO传输channel
		.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				// TODO Auto-generated method stub
				ch.pipeline()
				 .addLast(new IdleStateHandler(0, 0, RpcConstant.BEAT_INTERVAL * 3, TimeUnit.SECONDS))  // beat 3N, close if idle
                 .addLast(new HttpServerCodec())
				.addLast(new HttpObjectAggregator(RpcConstant.MAX_LENGTH))
				.addLast(new Nserverhandler(serializer,serverHandlerPool));
			}
		})
		.childOption(ChannelOption.SO_KEEPALIVE, true);
		 ChannelFuture f=b.bind().sync();
		 logger.info("#########################服务端成功启动");
		 f.channel().closeFuture().sync();
		
		} catch (Exception e) {
			// TODO: handle exception
			
			logger.error(e.getMessage(),e);
		}
		finally{
			 // stop
            try {
                serverHandlerPool.shutdown();    // shutdownNow
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
         
			try {
				bossGroup.shutdownGracefully().sync();
				workGroup.shutdownGracefully().sync();
			}
			catch(Exception e)
			{
				logger.error(e.getMessage());
			}
			logger.info("#########################服务端关闭了");
		}
		
		});
		 thread.setDaemon(true);    
	     thread.start();
	}
		/**
		 * 容器关闭的时候，要被调用xxxxxxxxxxxxxxx
		 * @Description:   
		 * @Creator: lhy
		 * @CreateTime: 2020年8月23日 上午11:21:54
		 * @Modifier: 
		 * @ModifyTime:
		 * @Reasons:
		 */
	    public void stop() {
	        // destroy server thread
	        if (thread != null && thread.isAlive()) {
	            thread.interrupt();
	        }
	        logger.info(">>>>>>>>>>> xxl-rpc remoting server destroy success.");
	    }
}
