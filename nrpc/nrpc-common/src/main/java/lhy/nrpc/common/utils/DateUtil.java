package lhy.nrpc.common.utils;

import java.util.Date;

public class DateUtil {
	/**
	 * 获得当前时间
	 * @Description:   
	 * @Creator: lhy
	 * @CreateTime: 2020年7月31日 下午1:37:21
	 * @Modifier: 
	 * @ModifyTime:
	 * @Reasons:
	 * @return
	 */
	public static long getNowTime(){
		return new Date().getTime();
	}
}
