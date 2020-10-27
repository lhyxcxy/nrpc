package lhy.nrpc.registry.common.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lhy.nrpc.common.exception.NRpcException;
import lhy.nrpc.common.reqres.http.BaseResponse;
import lhy.nrpc.registry.common.exception.NRpcRegistryException;

@Order(Integer.MIN_VALUE)
@RestControllerAdvice(basePackages="lhy.nrpc.registry")
public class NRpcRegistryExceptionHandler {
private static Logger logger =LoggerFactory.getLogger(NRpcRegistryExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<String>handleException(Exception e) {
    	logger.error("系统异常{0}", e);
        return BaseResponse.fail(e.getMessage());
    }

    @ExceptionHandler(NRpcException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<String> handleBaseRtunServiceException(NRpcException e) {
    	logger.error("BaseRtunServiceException系统异常{0}", e);
        return BaseResponse.fail(e.getMessage());
    }

    @ExceptionHandler(NRpcRegistryException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<String> handleBspsServiceException(NRpcRegistryException e) {
    	logger.error("BspsServiceException异常{0}", e);
        return BaseResponse.fail(e.getMessage());
    }


    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<String> exceptionHandler(BindException e) {
    	logger.error("BindException异常{0}", e);
        return BaseResponse.fail(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * 参数验证是否符合，如@NotEmpty(),@NotNull()
     *
     * @param e
     * @return
     * @Description:
     * @Creator: lhy
     * @CreateTime: 2020年7月20日 下午6:21:43
     * @Modifier:
     * @ModifyTime:
     * @Reasons:
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<String> exceptionHandler(MethodArgumentNotValidException e) {
    	logger.error("MethodArgumentNotValidException异常{0}", e);
        return BaseResponse.fail(e.getBindingResult().getFieldError().getDefaultMessage());
    }

}