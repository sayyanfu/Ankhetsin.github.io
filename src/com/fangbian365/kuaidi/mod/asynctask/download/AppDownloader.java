package com.fangbian365.kuaidi.mod.asynctask.download;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;
import android.widget.Toast;

import com.fangbian365.kuaidi.base.CHBApp;
import com.fangbian365.kuaidi.base.bean.DownloadItem;
import com.fangbian365.kuaidi.base.utils.FileUtils;
import com.fangbian365.kuaidi.base.utils.SysInfo;
import com.fangbian365.kuaidi.mod.asynctask.download.http.DownloadHttpListener;
import com.fangbian365.kuaidi.mod.asynctask.download.http.IHttpNotify;
import com.fangbian365.kuaidi.mod.asynctask.download.threadpool.ThreadPoolAppTask;
import com.fangbian365.kuaidi.mod.asynctask.download.threadpool.ThreadPoolManager;
import com.fangbian365.kuaidi.mod.dirmanager.DirType;


public class AppDownloader {
	
	private ArrayList<DownloadHttpListener> mDownloadListenerList;
	private static AppDownloader _instance = null;
	private Map<String, Integer> mRunningTask;
	private ThreadPoolManager mThreadPoolManager;
	
	private AppDownloader(){
		mDownloadListenerList = new ArrayList<DownloadHttpListener>();
		mRunningTask = Collections.synchronizedMap(new HashMap<String, Integer>());
		mThreadPoolManager = new ThreadPoolManager(3);
	}
	public static AppDownloader getInstance(){
		if(_instance == null)
			_instance = new AppDownloader();
		return _instance;
	}
	
	public void addNewDownloadTask(DownloadItem item, IHttpNotify listener) {
		IHttpNotify callback = listener == null ? new CallbackListener(item, true) : listener;
		if (null !=item && !TextUtils.isEmpty(item.getUrl())) {
			if(!mRunningTask.containsKey(item.getUrl())) {
				
				DownloadHttpListener downListener = new DownloadHttpListener(getFilePathByUrl(item.getUrl()), callback);
				downListener.setFileSize(item.getByte());
				
				ThreadPoolAppTask task = new ThreadPoolAppTask(item.getUrl(), downListener);
				mThreadPoolManager.execute(task);
				
				mRunningTask.put(item.getUrl(), item.getUrl().hashCode());
				mDownloadListenerList.add(downListener);
			} else {
				Toast.makeText(CHBApp.getAppContext(), "任务正在下载中", Toast.LENGTH_SHORT).show();
				
			}
		}
		
	}
	
	private String getFilePathByUrl(String url) {
		return FileUtils.getFilePathByUrl(url, SysInfo.getDirManager().getDirPath(DirType.DOWNLOAD));
	}
	
	public Map<String, Integer> getRunningTask() {
		return mRunningTask;
	}
	
	public void cancelDownloadTask(String url) {
		String path = FileUtils.getFilePathByUrl(url, SysInfo.getDirManager().getDirPath(DirType.DOWNLOAD));
		if(mDownloadListenerList != null && mDownloadListenerList.size() > 0) {
			for(DownloadHttpListener listener : mDownloadListenerList) {
				listener.cancel(path);
			}
		}
	}
	
	public void release() {
		mRunningTask.clear();
		mThreadPoolManager.shutDown();
	}
}
