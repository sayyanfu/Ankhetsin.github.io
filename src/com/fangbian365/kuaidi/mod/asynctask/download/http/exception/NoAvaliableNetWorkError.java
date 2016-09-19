package com.fangbian365.kuaidi.mod.asynctask.download.http.exception;

public class NoAvaliableNetWorkError extends Throwable
{
  private static final long serialVersionUID = 0x69f0f4719cd095f8L;

  public NoAvaliableNetWorkError()
  {
    super("没有可用网络");
  }
}