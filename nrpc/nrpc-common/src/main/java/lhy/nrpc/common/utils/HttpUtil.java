package lhy.nrpc.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lhy.nrpc.common.exception.NRpcException;
import lhy.nrpc.common.reqres.http.BaseResponse;

 
public class HttpUtil {
	private static Logger logger=LoggerFactory.getLogger(HttpUtil.class);
	/**
	 * post请求
	 * @Description:   
	 * @Creator: lhy
	 * @CreateTime: 2020年8月11日 上午11:03:49
	 * @Modifier: 
	 * @ModifyTime:
	 * @Reasons:
	 * @param strURL 请求地址
	 * @param jsonRequest json字符串传
	 * @return
	 */
    public static String post(String strURL, String jsonRequest) {
       
        BufferedReader reader = null;
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            //一定要用BufferedReader 来接收响应， 使用字节来接收响应的方法是接收不到内容的
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(jsonRequest);
            out.flush();
            out.close();
            // 读取响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder result=new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
            	result.append(line);
            }
            reader.close();
            //logger.debug("####"+result.toString());
            return result.toString();
        } catch (IOException e) {
           throw new NRpcException("http请求失败",e);
        }

    }
 
    /**
     * 转换为基本返回对象
     * @Description:   
     * @Creator: lhy
     * @CreateTime: 2020年8月11日 下午12:02:48
     * @Modifier: 
     * @ModifyTime:
     * @Reasons:
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
	public static BaseResponse toResponse(String response)
    {
    	return JSONUtil.json2Bean(response, BaseResponse.class);
    }
    /**
     * 判断是否返回成功
     * @Description:   
     * @Creator: lhy
     * @CreateTime: 2020年8月11日 下午12:02:40
     * @Modifier: 
     * @ModifyTime:
     * @Reasons:
     * @param response
     * @return
     */
    public static boolean isResponseSucess(String response)
    {
    	return BaseResponse.SUCCESS_CODE==JSONUtil.json2Bean(response,BaseResponse.class).getCode();
    }

}