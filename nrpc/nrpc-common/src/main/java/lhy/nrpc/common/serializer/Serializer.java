package lhy.nrpc.common.serializer;

/**
 * 序列化编解码接口
 * @Description:   
 * @author: lhy 
 * @date:   2020年7月31日 下午2:12:21   
 *
 */
public abstract class Serializer {
	
	public abstract <T> byte[] serialize(T obj);
	public abstract <T> Object deserialize(byte[] bytes, Class<T> clazz);

}
