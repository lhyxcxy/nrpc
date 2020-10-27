package lhy.nrpc.core.rpc.client.handler;





import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import lhy.nrpc.common.reqres.rpc.RpcResponse;
import lhy.nrpc.common.serializer.Serializer;
import lhy.nrpc.core.rpc.client.future.RpcFutureResponsePool;
/**
 * netty客户端handler
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午2:14:36   
 *
 */
public class NClientHandler extends SimpleChannelInboundHandler<FullHttpResponse>{

	    private Serializer serializer;
	    private RpcFutureResponsePool xxlRpcInvokerFactory;
	    public NClientHandler(RpcFutureResponsePool xxlRpcInvokerFactory, Serializer serializer) {
	        this.xxlRpcInvokerFactory = xxlRpcInvokerFactory;
	        this.serializer = serializer;
	    }
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
		// TODO Auto-generated method stub
		
		byte[] responseBytes = ByteBufUtil.getBytes(msg.content());
		RpcResponse xxlRpcResponse = (RpcResponse) serializer.deserialize(responseBytes, RpcResponse.class);
		System.out.println("客户端接收数据："+Thread.currentThread().getName()+"》"+xxlRpcResponse.toString());
		// notify response
        xxlRpcInvokerFactory.notifyInvokerFuture(xxlRpcResponse.getRequestId(), xxlRpcResponse);
	}

	
}
