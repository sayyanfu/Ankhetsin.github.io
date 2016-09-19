package com.fangbian365.kuaidi.mod.asynctask.download.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池管理类，最后不用退出时调用stop接口终止线程池
 * 根据这个管理类产生不同的线程池对象，如图片下载线程池，核心下载线程池
 * 
 * @author lruijun
 *
 */
public class ThreadPoolManager {

	private static final String TAG = "ThreadPoolManager";
	
	//线程池大小 
	private int poolSize;
	private static final int MIN_POOL_SIZE = 3;
	private static final int MAX_POOL_SIZE = 10;
	
	//线程池
	private ExecutorService threadPool;
	
	public ThreadPoolManager(int poolSize) {
		
		if (poolSize < MIN_POOL_SIZE) poolSize = MIN_POOL_SIZE;
		if (poolSize > MAX_POOL_SIZE) poolSize = MAX_POOL_SIZE;
		this.poolSize = poolSize;
		
		threadPool = Executors.newFixedThreadPool(this.poolSize);
	}
	
	public void execute(Runnable r) {
		threadPool.execute(r);
	}
	
	public void shutDown() {
		threadPool.shutdown();
	}
}
