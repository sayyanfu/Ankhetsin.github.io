package com.fangbian365.kuaidi.mod.asynctask.http;

/**
 * 
 * @author lruijun
 * @date   2015-06-02
 */
public class TaskResult {

	/** 任务返回的结果*/
	public TaskResultStatus status;
	
	/** 表示系统处理的结果，当status为OK时，是对结果的描述。当为FAILED时描述系统处理失败的原因 ,其他时候值为NULL*/
	public String result;
	
	public TaskResult(){
		status = TaskResultStatus.FAILED;
	}
	
}
