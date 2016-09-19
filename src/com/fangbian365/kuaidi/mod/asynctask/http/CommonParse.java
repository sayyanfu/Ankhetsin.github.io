package com.fangbian365.kuaidi.mod.asynctask.http;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.fangbian365.kuaidi.base.log.FrameLog;

/**
 * 公共解析
 * @author lruijun
 */
public class CommonParse {

	private static String TAG = "CommonParse";
	public static final String STATUS_KEY = "result"; // 
	public static final String DATA_KEY = "data";
	
	public static TaskResult parseCHBHttpJsonResponse(JSONObject json) throws JSONException, HttpException{
		FrameLog.d(TAG, json.toString());
		
		String returnCode = json.getString(STATUS_KEY);
		TaskResult result;

		switch (Result.toCode(returnCode)) {
		case RQST_SUCCESS:
			result = new TaskResult();
			if (json.has(DATA_KEY)) {
				result.result = json.toString();
			}
			result.status = TaskResultStatus.OK;
			return result;
		case RQST_FAIL:
			result = new TaskResult();
			result.result = json.toString();
			result.status = TaskResultStatus.FAILED;
			return result;
		default:
			throw new HttpException("数据请求失败");
		}
	}
	
	public enum Result {
		RQST_SUCCESS, RQST_FAIL, NOVALUE;

		public static Result toCode(String str) {
			try {
				if(str.equals("ok")) {
					return RQST_SUCCESS;
				}
				return RQST_FAIL;
			} catch (Exception ex) {
				return NOVALUE;
			}
		}
	}
}