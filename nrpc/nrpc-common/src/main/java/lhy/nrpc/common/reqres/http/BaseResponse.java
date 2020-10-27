package lhy.nrpc.common.reqres.http;


import java.io.Serializable;

/**
 * http返回实体
 * @Description:   
 * @author: lhy 
 * @date:   2020年8月7日 下午4:26:36   
 *
 */
public class BaseResponse<T> implements Serializable {
	public static final long serialVersionUID = 42L;

	public static final int SUCCESS_CODE = 200;
	public static final int FAIL_CODE = 500;

	public static final BaseResponse<String> SUCCESS = new BaseResponse<>(null);
	public static final BaseResponse<String> FAIL = new BaseResponse<>(FAIL_CODE, null);

	private int code;
	private String msg;
	private T data;
	  /**
     * 其他数据
     */
    private Object otherd;
    /**
     * 会话唯一id
     */
    private String sid;

	public BaseResponse(){}
	public BaseResponse(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public BaseResponse(T data) {
		this.code = SUCCESS_CODE;
		this.data = data;
	}
	private static <T> BaseResponse<T> getR() {
        return new BaseResponse<T>();
    }
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}


    public BaseResponse<T> otherd(String otherd) {
        this.otherd = otherd;
        return this;
    }

    public BaseResponse<T> sid(String sid) {
        this.sid = sid;
        return this;
    }


    public Object getOtherd() {
        return otherd;
    }

    public String getSid() {
        return sid;
    }

    public static <T> BaseResponse<T> data(T t) {
        BaseResponse<T> r = getR();
        r.setCode(SUCCESS_CODE);
        r.setData(t);
        return r;
    }

    public static <T> BaseResponse<T> data(T t, String msg) {
        BaseResponse<T> r = getR();
        r.setCode(SUCCESS_CODE);
        r.setData(t);
        r.setMsg(msg);
        return r;
    }

    public static <T> BaseResponse<T> success(String msg) {
        BaseResponse<T> r = getR();
        r.setCode(SUCCESS_CODE);
        r.setMsg(msg);
        return r;
    }

    public static <T> BaseResponse<T> fail(String msg) {
        BaseResponse<T> r = getR();
        r.setCode(FAIL_CODE);
        r.setMsg(msg);
        return r;
    }

    public static <T> BaseResponse<T> fail(int code, String msg) {
        BaseResponse<T> r = getR();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public static <T> BaseResponse<T> status(boolean code) {
        return code ? success("操作成功") : fail("操作失败");
    }
	@Override
	public String toString() {
		return "BaseRt [code=" + code + ", msg=" + msg + ", data=" + data + ", otherd=" + otherd + ", sid=" + sid + "]";
	}
	

}
