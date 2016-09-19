package com.fangbian365.kuaidi.mod.asynctask.download.http;


public class NetworkResult
{
  public int mStatusCode = -1;

  public long mContentLength = -1L;

  public boolean mIsConnected = false;
  public NetworkError mNetworkError;
}