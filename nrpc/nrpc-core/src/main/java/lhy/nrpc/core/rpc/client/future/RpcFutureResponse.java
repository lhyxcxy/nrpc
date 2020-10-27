package lhy.nrpc.core.rpc.client.future;


import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lhy.nrpc.common.exception.NRpcException;
import lhy.nrpc.common.reqres.rpc.RpcRequest;
import lhy.nrpc.common.reqres.rpc.RpcResponse;

/**
 * call back future
 *
 * @author xuxueli 2015-11-5 14:26:37
 */
public class RpcFutureResponse implements Future<RpcResponse> {


    // net data
    private RpcRequest request;
    private RpcResponse response;

    // future lock
    private boolean done = false;
    private Object lock = new Object();


    public RpcFutureResponse(RpcRequest request) {
 
        this.request = request;

        // set-InvokerFuture
        setInvokerFuture();
    }
    
    public void setInvokerFuture() {
    	RpcFutureResponsePool.getInstance().setInvokerFuture(request.getRequestId(), this);
    }

    public void removeInvokerFuture() {
    	RpcFutureResponsePool.getInstance().removeInvokerFuture(request.getRequestId());
    }

    // ---------------------- get ----------------------

    public RpcRequest getRequest() {
        return request;
    }



    // ---------------------- for invoke back ----------------------

    public void setResponse(RpcResponse response) {
        this.response = response;
        synchronized (lock) {
            done = true;
            lock.notifyAll();
        }
    }


    // ---------------------- for invoke ----------------------

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        // TODO
        return false;
    }

    @Override
    public boolean isCancelled() {
        // TODO
        return false;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public RpcResponse get() throws InterruptedException {
        try {
            return get(-1, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new NRpcException(e);
        }
    }

    @Override
    public RpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        if (!done) {
            synchronized (lock) {
                try {
                    if (timeout < 0) {
                        lock.wait();
                    } else {
                        long timeoutMillis = (TimeUnit.MILLISECONDS == unit) ? timeout : TimeUnit.MILLISECONDS.convert(timeout, unit);
                        lock.wait(timeoutMillis);
                    }
                } catch (InterruptedException e) {
                    throw e;
                }
            }
        }

        if (!done) {
            throw new NRpcException("rpc, request timeout at:" + System.currentTimeMillis() + ", request:" + request.toString());
        }
        return response;
    }


}
