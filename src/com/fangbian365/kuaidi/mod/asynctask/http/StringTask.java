package com.fangbian365.kuaidi.mod.asynctask.http;

import com.fangbian365.kuaidi.base.volley.Response.ErrorListener;
import com.fangbian365.kuaidi.base.volley.Response.Listener;
import com.fangbian365.kuaidi.base.volley.toolbox.StringRequest;

public class StringTask extends StringRequest {

	public StringTask(int method, String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, listener, errorListener);
	}
	
	public StringTask(String url, Listener<String> listener, ErrorListener errorListener) {
		super(url, listener, errorListener);
    }
	
	
}
