package lhy.nrpc.registry.common.exception;

import lhy.nrpc.common.exception.NRpcException;


public class NRpcRegistryException extends NRpcException {

    private static final long serialVersionUID = 42L;

    public NRpcRegistryException(String msg) {
        super(msg);
    }

    public NRpcRegistryException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NRpcRegistryException(Throwable cause) {
        super(cause);
    }

}