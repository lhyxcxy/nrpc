package lhy.nrpc.core.rpc.client;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.timeout.IdleStateHandler;
import lhy.nrpc.common.constant.RpcConstant;
import lhy.nrpc.common.exception.NRpcException;
import lhy.nrpc.common.reqres.rpc.RpcRequest;
import lhy.nrpc.common.reqres.rpc.RpcResponse;
import lhy.nrpc.common.serializer.Serializer;
import lhy.nrpc.core.factory.NclientFactory;
import lhy.nrpc.core.rpc.client.future.RpcFutureResponse;
import lhy.nrpc.core.rpc.client.future.RpcFutureResponsePool;
import lhy.nrpc.core.rpc.client.handler.NClientHandler;
/**
 *  netty 客户端连接
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午2:13:15   
 *
 */
public class Nclient {
	Logger logger=LoggerFactory.getLogger(Nclient.class);
	private  String address;
	private  int port;
	private String host;
	private  Channel channel;
	private  Serializer serializer;
	private EventLoopGroup group;
	public Nclient(String address, Serializer serializer) throws MalformedURLException {
		super();
		this.address = address;
		this.serializer = serializer;
		 if (!address.toLowerCase().startsWith("http")) {
	            address = "http://" + address;    // IP:PORT, need parse to url
	        }

	        this.address = address;
	        URL url = new URL(address);
	        this.host = url.getHost();
	        this.port = url.getPort() > -1 ? url.getPort() : 80;
	}

	
	public String getAddress() {
		return address;
	}


	public void init() throws InterruptedException {
		
		group=new NioEventLoopGroup(2);
		try {
		Bootstrap b=new Bootstrap();
		b.group(group)
		//.channel(NioDatagramChannel.class)//udp
		.channel(NioSocketChannel.class)
		.remoteAddress(new InetSocketAddress(this.host,this.port))
		.handler(new ChannelInitializer<NioSocketChannel>(){

			@Override
			protected void initChannel(NioSocketChannel ch) throws Exception {
				// TODO Auto-generated method stub
				ch.pipeline()
				 .addLast(new IdleStateHandler(0, 0, RpcConstant.BEAT_INTERVAL, TimeUnit.SECONDS))   // beat N, close if fail
                 .addLast(new HttpClientCodec())
				.addLast(new HttpObjectAggregator(RpcConstant.MAX_LENGTH))
				.addLast(new NClientHandler(RpcFutureResponsePool.getInstance(),serializer));
			}
		}) 
		.option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
		ChannelFuture cf=b.connect().sync();
		this.channel=cf.channel();
		
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(),e);
			close();
		}
		finally {
			// valid
	        if (!isValidate()) {
	            close();
	            return;
	        }
		}
	}

    public boolean isValidate() {
        if (this.channel != null) {
            return this.channel.isActive();
        }
        return false;
    }


   
    public void close() throws InterruptedException {
    	NclientFactory.remove(this.address);//删除池中的链接对象
        if (this.channel != null && this.channel.isActive()) {
            this.channel.close();        // if this.channel.isOpen()
        }
        if (this.group != null && !this.group.isShutdown()) {
            this.group.shutdownGracefully().sync();
        }
        logger.info(">>>>>>>>>>>netty client close.");
    }


    
    private void sendToServer(RpcRequest xxlRpcRequest) throws InterruptedException, URISyntaxException {
        byte[] requestBytes = serializer.serialize(xxlRpcRequest);

        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, new URI(address).getRawPath(), Unpooled.wrappedBuffer(requestBytes));
        request.headers().set(HttpHeaderNames.HOST, host);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

        this.channel.writeAndFlush(request).sync();
    }
    
    public RpcResponse send(RpcRequest xxlRpcRequest) {
    	RpcFutureResponse futureResponse = new RpcFutureResponse(xxlRpcRequest);
    	RpcResponse response=null;
    	try {
    	sendToServer(xxlRpcRequest);
    	response=futureResponse.get();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new NRpcException("发送失败",e);
		}
    	finally {
    		futureResponse.removeInvokerFuture();
		}
    	return response;
    }
    
    public RpcResponse send(RpcRequest xxlRpcRequest,long timeout, TimeUnit unit)  {
    	RpcFutureResponse futureResponse = new RpcFutureResponse(xxlRpcRequest);
    	RpcResponse response=null;
    	
    	try {
    	sendToServer(xxlRpcRequest);
    	if(timeout<=0||unit==null)
    		response=futureResponse.get();
    	else
    		response=futureResponse.get(timeout,unit);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new NRpcException("发送失败",e);
		}
    	finally {
    		futureResponse.removeInvokerFuture();
		}
    	return response;
    }

	
}
