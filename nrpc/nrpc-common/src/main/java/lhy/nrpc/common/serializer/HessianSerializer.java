package lhy.nrpc.common.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import lhy.nrpc.common.exception.NRpcException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hessian序列化编解码
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午2:11:01   
 *
 */
public class HessianSerializer extends Serializer {

	@Override
	public <T> byte[] serialize(T obj){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Hessian2Output ho = new Hessian2Output(os);
		try {
			ho.writeObject(obj);
			ho.flush();
			byte[] result = os.toByteArray();
			return result;
		} catch (IOException e) {
			throw new NRpcException(e);
		} finally {
			try {
				ho.close();
			} catch (IOException e) {
				throw new NRpcException(e);
			}
			try {
				os.close();
			} catch (IOException e) {
				throw new NRpcException(e);
			}
		}

	}

	@Override
	public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		Hessian2Input hi = new Hessian2Input(is);
		try {
			Object result = hi.readObject();
			return result;
		} catch (IOException e) {
			throw new NRpcException(e);
		} finally {
			try {
				hi.close();
			} catch (Exception e) {
				throw new NRpcException(e);
			}
			try {
				is.close();
			} catch (IOException e) {
				throw new NRpcException(e);
			}
		}
	}
	
}
