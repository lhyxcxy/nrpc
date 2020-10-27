package lhy.nrpc.common.exception;

/**
 * rpc异常类
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午1:57:55   
 *
 */
public class NRpcException extends RuntimeException {

    private static final long serialVersionUID = 42L;

    public NRpcException(String msg) {
        super(msg);
    }

    public NRpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NRpcException(Throwable cause) {
        super(cause);
    }

}