package com.fangbian365.kuaidi.mod.asynctask.download.http;


public enum NetworkError
{
  SUCCESS(1), 
  FAIL_UNKNOWN(-1), 
  FAIL_CONNECT_TIMEOUT(-2), 
  FAIL_NOT_FOUND(-3), 
  FAIL_IO_ERROR(-4), 
  CANCEL(-5), 
  NO_AVALIABLE_NETWORK(-6),
  FILE_NOT_FOUND(-7);

  private int value;

  private NetworkError(int value) { this.value = value; }

  public int getValue()
  {
    return this.value;
  }
}