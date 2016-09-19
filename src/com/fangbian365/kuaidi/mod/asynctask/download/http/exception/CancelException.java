package com.fangbian365.kuaidi.mod.asynctask.download.http.exception;

public class CancelException extends Throwable
{
  private static final long serialVersionUID = 0xb9ccbb0c99d91771L;

  public CancelException()
  {
    super("取消下载");
  }
}
