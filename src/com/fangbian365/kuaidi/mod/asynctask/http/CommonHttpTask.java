package com.fangbian365.kuaidi.mod.asynctask.http;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.fangbian365.kuaidi.base.CHBApp;
import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.UrlUtil;
import com.fangbian365.kuaidi.base.volley.DefaultRetryPolicy;
import com.fangbian365.kuaidi.base.volley.VolleyError;
import com.fangbian365.kuaidi.base.volley.Response.ErrorListener;
import com.fangbian365.kuaidi.base.volley.Response.Listener;

public abstract class CommonHttpTask extends BaseTask {
private static final String TAG = "CommonHttpTask";
	
	/** 请求的Url */
	protected String mUrl;
	/** 请求参数 */
	protected Map<String, String> httpParams;

	/** json请求 */
	private NormalPostTask mNormalPastRequest;
	/** 运行状态 */
	protected boolean isRunning = false;
	
	/**
	 * 新建网络请求任务
	 * 
	 * @param baseUrl
	 *            请求网址
	 * @param value
	 *            参数
	 */
	public CommonHttpTask(String url, Map<String, String> paramsMap) {
		mUrl = url;
		httpParams = UrlUtil.getCommRqstParams(paramsMap);
		FrameLog.d(TAG, mUrl);
		for (Entry<String, String> s : httpParams.entrySet()) {
			FrameLog.d(TAG, "参数**** " + s);
		}
	}
	
	/** 执行任务 */
	public void execute() {
		
		mNormalPastRequest = new NormalPostTask(mUrl, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				TaskResult tr = new TaskResult();
				try {
					tr = _parseResponse(response);
				} catch (JSONException e) {
					e.printStackTrace();
					tr.status = TaskResultStatus.JSON_ERROR;
				} catch (HttpException e) {
					e.printStackTrace();
					tr.status = TaskResultStatus.HTTP_ERROR;
				}
				isRunning = false;
				onPostExecute(tr);
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				isRunning = false;

				TaskResult tr = new TaskResult();
				tr.status = TaskResultStatus.HTTP_ERROR;
				onPostExecute(tr);

				FrameLog.e(TAG, "onErrorResponse msg=" + error.toString());
			}
		}, httpParams);

		mNormalPastRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		MainRequestQueue.getInstance(CHBApp.getAppContext()).addToQueue(mNormalPastRequest);

		isRunning = true;
		onPreExecute();

	}
	
	
	/** 取消任务 */
	public void cancel() {
		if (mNormalPastRequest != null) {
			mNormalPastRequest.cancel();
		}
	}

	/** 是否在运行 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * 用来初步解析返回结果Response
	 * 
	 * @param res
	 *            返回结果
	 * @return
	 */
	protected abstract TaskResult _parseResponse(JSONObject json) throws JSONException, HttpException;
}
