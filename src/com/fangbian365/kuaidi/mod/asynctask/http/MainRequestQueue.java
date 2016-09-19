package com.fangbian365.kuaidi.mod.asynctask.http;

import android.content.Context;

import com.fangbian365.kuaidi.base.volley.Request;
import com.fangbian365.kuaidi.base.volley.RequestQueue;
import com.fangbian365.kuaidi.base.volley.toolbox.Volley;

/**
 * 主请求队列
 * @author lruijun
 * @date 2015.06.04
 */
public class MainRequestQueue {

	private Context mContext;
	private RequestQueue mRequestQueue;
	
	private static MainRequestQueue mInstance = null;
	
	/** 获取主请求的单例对象 */
	public static synchronized MainRequestQueue getInstance(Context context){
		if(mInstance == null ){
			mInstance = new MainRequestQueue(context);
		}
		return mInstance;
	}
	
	private MainRequestQueue(Context context){
		mContext = context.getApplicationContext();
	}
	
	public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

	/** 将请求加入到队列中 */
    public <T> void addToQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
