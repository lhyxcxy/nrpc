package lhy.nrpc.core.rpc.server.handler;

import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import lhy.nrpc.common.reqres.rpc.RpcRequest;
import lhy.nrpc.common.reqres.rpc.RpcResponse;
import lhy.nrpc.common.serializer.Serializer;
import lhy.nrpc.core.factory.RpcServiceFactory;
/**
 * netty server handler
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午2:15:41   
 *
 */

public class Nserverhandler  extends SimpleChannelInboundHandler<FullHttpRequest>{
	Logger logger =LoggerFactory.getLogger(Nserverhandler.class);
	private  Serializer serializer;
	private ThreadPoolExecutor serverHandlerPool;
	
	public Nserverhandler(Serializer serializer,ThreadPoolExecutor serverHandlerPool) {
		super();
		this.serializer = serializer;
		this.serverHandlerPool=serverHandlerPool;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		// TODO Auto-generated method stub
		// request parse
        final byte[] requestBytes = ByteBufUtil.getBytes(msg.content());    // byteBuf.toString(io.netty.util.CharsetUtil.UTF_8);
        //final String uri = msg.uri();
        final boolean keepAlive = HttpUtil.isKeepAlive(msg);
		
		// do invoke
        serverHandlerPool.execute(new Runnable() {
            @Override
            public void run() {
                process(ctx,requestBytes,keepAlive);
            }
        });
		
	}
	
	private void process(ChannelHandlerContext ctx,byte[] requestBytes,boolean keepAlive){
		RpcRequest rpcRequest = (RpcRequest) serializer.deserialize(requestBytes, RpcRequest.class);
		logger.info("服务端收到数据："+Thread.currentThread().getName()+"》"+rpcRequest.toString());
		RpcResponse rpcResponse=new RpcResponse();
		rpcResponse.setRequestId(rpcRequest.getRequestId());
		rpcResponse.setErrorMsg("ok");
		Object resultObj="";
		try {
			resultObj = RpcServiceFactory.execServiceMethod(rpcRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			rpcResponse.setErrorMsg(e.getMessage());
		} 
		rpcResponse.setResult(resultObj);
		byte[] responseBytes = serializer.serialize(rpcResponse);
		logger.info("服务端返回数据："+rpcResponse.toString());
		writeResponse(ctx, keepAlive, responseBytes);
	}
	
	/**
     * write response
     */
    private void writeResponse(ChannelHandlerContext ctx, boolean keepAlive, byte[] responseBytes){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseBytes));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");       // HttpHeaderValues.TEXT_PLAIN.toString()
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.writeAndFlush(response);
    }
	

}
