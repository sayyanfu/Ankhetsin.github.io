package com.fangbian365.kuaidi.mod.asynctask.download.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.fangbian365.kuaidi.mod.asynctask.download.http.exception.CancelException;
import com.fangbian365.kuaidi.mod.asynctask.download.http.exception.StatusCodeError;

public class HttpProvider {
	private static final String CHARSET = "UTF-8";
	private static HttpClient customerHttpClient;
	
	/** 
     * 最大连接数 
     */  
    public final static int MAX_TOTAL_CONNECTIONS = 400;  
    /** 
     * 获取连接的最大等待时间 
     */  
    public final static int WAIT_TIMEOUT = 6000;  
    /** 
     * 每个路由最大连接数 
     */  
    public final static int MAX_ROUTE_CONNECTIONS = 400;  
    /** 
     * 连接超时时间 
     */  
    public final static int CONNECT_TIMEOUT = 8000;  
    /** 
     * 读取超时时间 
     */  
    public final static int READ_TIMEOUT = 8000;  
    

	public static void freeHttpClient(){
    	if (customerHttpClient != null) {
			customerHttpClient.getConnectionManager().shutdown();
			customerHttpClient = null;
		}  	
    }
	public static NetworkResult doGet(String uri,HttpProviderListener listener) {
		return doFetch(true, uri,listener, null, new NameValuePair[0]);
	}

	public static NetworkResult doPost(String uri,
			HttpProviderListener listener, NameValuePair[] pairs) {
		return doFetch(false, uri,listener, null, pairs);
	}

	public static NetworkResult doGet(String uri,
			HttpProviderListener listener, HashMap<String, String> headers) {
		return doFetch(true, uri,listener, headers, new NameValuePair[0]);
	}

	public static NetworkResult doPost(String uri,
			HttpProviderListener listener,
			HashMap<String, String> headers, NameValuePair[] pairs) {
		return doFetch(false, uri,listener, headers, pairs);
	}

	private static NetworkResult doFetch(boolean isGet, String uri,
			HttpProviderListener listener,
			HashMap<String, String> headers, NameValuePair[] pairs) {
		NetworkResult result = new NetworkResult();
		result.mNetworkError = NetworkError.SUCCESS;

		InputStream is = null;
		try {
//			if (UiUtil.isNetworkAvailable(context)) {
//				result.mNetworkError = NetworkError.NO_AVALIABLE_NETWORK;
//				notifyError(listener, result, new NoAvaliableNetWorkError());
//			}

			HttpUriRequest request = null;
			if (isGet) {
				request = new HttpGet(uri);
			} else {
				request = new HttpPost(uri);
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				for (NameValuePair pair : pairs) {
					parameters.add(pair);
				}

				UrlEncodedFormEntity entity = null;
				try {
					entity = new UrlEncodedFormEntity(parameters, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					result.mNetworkError = NetworkError.FAIL_UNKNOWN;

					notifyError(listener, result, e);
				}
				((HttpPost) request).setEntity(entity);
			}

			//获取断点续传位置
			int pos = 0;
			if (listener != null && (listener instanceof DownloadHttpListener)) {
				pos = (int)((DownloadHttpListener)listener).getCurBreakPos();
			}
			if (pos > 0) {
				request.addHeader("Range", "bytes=" + pos + "-");
			}

			if ((headers != null) && (headers.size() > 0)) {
				Iterator keyIterator = headers.keySet().iterator();
				while (keyIterator.hasNext()) {
					String name = (String) keyIterator.next();
					String value = (String) headers.get(name);
					request.addHeader(name, value);
				}
			}

			HttpClient client = getHttpClient();			
			HttpResponse response = null;
			try {
				response = client.execute(request);
			} catch (ClientProtocolException e) {
				result.mNetworkError = NetworkError.FAIL_UNKNOWN;
				notifyError(listener, result, e);
			} catch (IOException e) {
				result.mNetworkError = NetworkError.FAIL_IO_ERROR;
				notifyError(listener, result, e);	
			} catch (Throwable e) {
				result.mNetworkError = NetworkError.FAIL_CONNECT_TIMEOUT;
				notifyError(listener, result, e);
			}

			int statusCode = response.getStatusLine().getStatusCode();
			result.mStatusCode = statusCode;
			if ((statusCode != 200) && (statusCode != 206)) {
				result.mNetworkError = NetworkError.FAIL_NOT_FOUND;

				notifyError(listener, result, new StatusCodeError(statusCode));
			}
			result.mIsConnected = true;

			HttpEntity responseEntity = response.getEntity();
			long contentLen = responseEntity.getContentLength();
			result.mContentLength = contentLen;
			if ((listener != null) && (!listener.onStart(contentLen))) {
				result.mNetworkError = NetworkError.CANCEL;
				notifyError(listener, result, new CancelException());
			}

			try {
				is = responseEntity.getContent();
			} catch (IllegalStateException e) {
				result.mNetworkError = NetworkError.FAIL_IO_ERROR;
				notifyError(listener, result, e);
			} catch (IOException e) {
				result.mNetworkError = NetworkError.FAIL_IO_ERROR;

				notifyError(listener, result, e);
			}

			byte[] buf = new byte[1024];
			int len = -1;
			try {
				while ((len = is.read(buf, 0, 1024)) != -1) {
					if ((listener == null) || (listener.onAdvance(buf, 0, len)))
						continue;
					result.mNetworkError = NetworkError.CANCEL;
					notifyError(listener, result, new CancelException());
				}
			} catch (IOException e) {
				result.mNetworkError = NetworkError.FAIL_IO_ERROR;
				notifyError(listener, result, e);
			}

			if (listener != null)
				listener.onFinished();
		} catch (Throwable e) {
			result.mNetworkError = NetworkError.CANCEL;
			if (!(e instanceof CancelException)) {
				result.mNetworkError = NetworkError.FAIL_UNKNOWN;
				try {
					notifyError(listener, result, e);
				} catch (Throwable e1) {
					e1.printStackTrace();
				}

			}

			if (is != null)
				try {
					is.close();
				} catch (IOException localIOException1) {
				}
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException localIOException2) {
				}
		}
		return result;
	}

	private static void notifyError(HttpProviderListener listener,
			NetworkResult result, Throwable e) throws Throwable {
		if ((e != null) && ((e instanceof TimeoutException))) {
			result.mNetworkError = NetworkError.FAIL_CONNECT_TIMEOUT;
		}

		if (listener != null) {
			listener.onFail(result, e);
		}
		
		//打印出异常信息
		if (e != null) {
			e.printStackTrace();
		}
		
		//取消网络请求
		throw new CancelException();
	}

	private static synchronized HttpClient getHttpClient() {
		if (customerHttpClient == null) {
			HttpParams params = new BasicHttpParams();

			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, "UTF-8");
			HttpProtocolParams.setUseExpectContinue(params, true);

			HttpProtocolParams
					.setUserAgent(
							params,
							"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");

			// 设置最大连接数
			ConnManagerParams.setMaxTotalConnections(params, MAX_TOTAL_CONNECTIONS);	
			// 设置获取连接的最大等待时间
			ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);
			// 设置连接超时时间
			HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
			// 设置读取超时时间
			HttpConnectionParams.setSoTimeout(params, READ_TIMEOUT);
			
			// 设置每个路由最大连接数  
	        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS);  
	        ConnManagerParams.setMaxConnectionsPerRoute(params,connPerRoute);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			customerHttpClient = new DefaultHttpClient(conMgr, params);

			customerHttpClient.getParams().setParameter(
					"http.connection.timeout", Integer.valueOf(CONNECT_TIMEOUT));

			customerHttpClient.getParams().setParameter("http.socket.timeout",
					Integer.valueOf(CONNECT_TIMEOUT));
		}
		return customerHttpClient;
	}
}