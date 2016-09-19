package com.fangbian365.kuaidi.mod.asynctask.download.threadpool;

import com.fangbian365.kuaidi.base.log.FrameLog;

/**
 * 线程池中的任务单元，继承此类可以衍生出不同的任务类型
 * 具体的操作请在run函数里实现
 * @author lruijun
 *
 */
public abstract class ThreadPoolTask implements Runnable {

	protected String mUrl;
	
	public ThreadPoolTask(String url) {
		mUrl = url;
		FrameLog.d("ThreadPoolTask", mUrl);
	}
	
	public abstract void run();
	
	public String getURL() {
		return mUrl != null ? mUrl.trim() : mUrl;
	}
}
