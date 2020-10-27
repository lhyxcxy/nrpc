package lhy.nrpc.common.utils;

import java.util.concurrent.*;

import lhy.nrpc.common.exception.NRpcException;

/**
 * 线程池帮助类
 * @Description:   
 * @author: lhy 
 * @date:   2020年8月6日 上午10:32:08   
 *
 */
public class ThreadPoolUtil {

    /**
     * 创建线程池 
     * @Description:   
     * @Creator: lhy
     * @CreateTime: 2020年8月6日 上午10:33:38
     * @Modifier: 
     * @ModifyTime:
     * @Reasons:
     * @param threadName 线程池名字
     * @param corePoolSize 核心线程数
     * @param maxPoolSize 最大线程数
     * @param quques 任务队列的长度
     * @return
     */
    public static ThreadPoolExecutor makeThreadPool(final String threadName, int corePoolSize, int maxPoolSize,int quques) {
        ThreadPoolExecutor serverHandlerPool = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(quques),
                r -> new Thread(r, "nrpc, " + threadName + "-serverHandlerPool-" + r.hashCode()),
                (r, executor) -> {
                    throw new NRpcException("nrpc " + threadName + " Thread pool is EXHAUSTED!");
                });        

        return serverHandlerPool;
    }

}
