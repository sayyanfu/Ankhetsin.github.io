package com.fangbian365.kuaidi.mod.asynctask.http;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.fangbian365.kuaidi.base.volley.AuthFailureError;
import com.fangbian365.kuaidi.base.volley.NetworkResponse;
import com.fangbian365.kuaidi.base.volley.ParseError;
import com.fangbian365.kuaidi.base.volley.Request;
import com.fangbian365.kuaidi.base.volley.Response;
import com.fangbian365.kuaidi.base.volley.Response.ErrorListener;
import com.fangbian365.kuaidi.base.volley.Response.Listener;
import com.fangbian365.kuaidi.base.volley.toolbox.HttpHeaderParser;

public class NormalPostTask extends Request<JSONObject> {

	private Listener<JSONObject> mListener;
	private Map<String, String> mParams;
	
	public NormalPostTask(String url, Listener<JSONObject> listener, ErrorListener errorListener, Map<String, String> requestParams) {
		super(Request.Method.POST, url, errorListener);
		mListener = listener;
		mParams = requestParams;
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                 
            return Response.success(new JSONObject(jsonString),HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mParams;
	}
	
	@Override
	protected void deliverResponse(JSONObject response) {
		mListener.onResponse(response);
	}
	
	

}
