package com.fangbian365.kuaidi.mod.asynctask;

import java.util.Map;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.fangbian365.kuaidi.mod.asynctask.http.CommonHttpTask;
import com.fangbian365.kuaidi.mod.asynctask.http.CommonParse;
import com.fangbian365.kuaidi.mod.asynctask.http.TaskResult;


/**
 * 基础任务类
 * @author lruijun
 *
 */
public class CHBHttpTask extends CommonHttpTask{

	public CHBHttpTask(String url, Map<String, String> params) {
		super(url, params);
	}

	@Override
	protected TaskResult _parseResponse(JSONObject json) throws JSONException,
			HttpException {
		
		return CommonParse.parseCHBHttpJsonResponse(json);
	}
	
}