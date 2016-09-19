package com.fangbian365.kuaidi.mod.asynctask.http;
/**
 * Task执行的结果
* @author lruijun
* @date   2015-06-02
*/
public enum TaskResultStatus {
	OK,
	FAILED, 
	NET_ERROR,
	HTTP_ERROR,
	JSON_ERROR,
	IO_ERROR,
}