package com.fangbian365.kuaidi.mod.asynctask.download.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 简单的网络数据下载监听接口，可配合DataFetchedListener进行进度显示
 * @author lruijun
 *
 */
public class DataHttpListener implements HttpProviderListener {
	private String mTag;
	private DataFetchedListener mDataHttpListener;
	//数据缓存区
	private ByteArrayOutputStream baos = null; 
	
	private long mContentLen = -1L;
	private long mPos = 0L;
	
	public DataHttpListener() {
	}

	public DataHttpListener(String tag, DataFetchedListener dataFetchedListener) {
		this.mTag = tag;
		this.mDataHttpListener = dataFetchedListener;
	}

	@Override
	public boolean onStart(long contentLen) {
		this.baos = new ByteArrayOutputStream();
		this.mContentLen = contentLen;
		return true;
	}
	@Override
	public boolean onAdvance(byte[] buf, int start, int len) {
		if (this.baos == null)
			this.baos = new ByteArrayOutputStream();
		this.baos.write(buf, start, len);

		this.mPos += len;
		if (this.mDataHttpListener != null)
			this.mDataHttpListener.onAdvance(this.mContentLen, this.mPos);
		return true;
	}
	
	public long getCursize(){
		return this.mPos;
	}
	
	@Override
	public long getFileSize() {
		return this.mContentLen;
	}
	
	@Override
	public void onFinished() {
		if (this.mDataHttpListener != null) {
			this.mDataHttpListener.onFetched(this.mTag, this.baos.toByteArray());
			clearBuffer(); //默认在回调中清空数据
		}
		
	}
	@Override
	public void onFail(NetworkResult result, Throwable e) {
		clearBuffer();
		if (this.mDataHttpListener != null)
			this.mDataHttpListener.onFail(this.mTag);
	}

	public void clearBuffer() {
		try {
			if (this.baos != null)
				this.baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.baos = null;
	}

	public byte[] getData() {
		if (this.baos != null) {
			//与onFinished重复 此处暂不回调
//			if (this.mDataHttpListener != null)
//				this.mDataHttpListener.onFetched(this.mTag,
//						this.baos.toByteArray());
			return this.baos.toByteArray();
		}
		return null;
	}


	public static abstract interface DataFetchedListener {
		public abstract void onFetched(String tag,
				byte[] paramArrayOfByte);

		public abstract void onFail(String tag);

		public abstract void onAdvance(long totalSize, long curSize);
	}

}