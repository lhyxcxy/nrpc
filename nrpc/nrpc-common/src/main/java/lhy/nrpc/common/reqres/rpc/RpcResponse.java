package lhy.nrpc.common.reqres.rpc;

import java.io.Serializable;

/**
 * RPC返回消息对象
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午2:10:44   
 *
 */
public class RpcResponse implements Serializable {
	private static final long serialVersionUID = 42L;


	private String requestId;
    private String errorMsg;
    private Object result;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "XxlRpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", result=" + result +
                '}';
    }

}
