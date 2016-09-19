package com.fangbian365.kuaidi.mod.asynctask.http;

/**
 * 只处理返回结果的简单监听
 * @author lruijun
 *
 */
public abstract class TaskSimpleListener implements TaskListener {

	public abstract void onTaskFinished(BaseTask task, TaskResult result);

	@Override
	public void onPostExecute(BaseTask task, TaskResult result) {
		onTaskFinished(task, result);
	}

	@Override
	public void onProgressUpdate(BaseTask task, Object param) {

	}

	@Override
	public void onCancelled(BaseTask task) {

	}

}
