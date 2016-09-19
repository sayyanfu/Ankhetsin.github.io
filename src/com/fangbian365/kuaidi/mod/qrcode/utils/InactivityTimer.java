/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fangbian365.kuaidi.mod.qrcode.utils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;


/**
 * Finishes an activity after a period of inactivity if the device is on battery
 * power.
 */
public class InactivityTimer {

	private static final String TAG = InactivityTimer.class.getSimpleName();

	private static final long INACTIVITY_DELAY_MS = 2 * 60 * 1000L;

	private AsyncTask<Object, Object, Object> inactivityTask;

	public InactivityTimer() {
		onActivity();
	}

	@SuppressLint("NewApi")
	public synchronized void onActivity() {
		cancel();
		inactivityTask = new InactivityAsyncTask();
		if (Build.VERSION.SDK_INT >= 11) {
			inactivityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			inactivityTask.execute();
		}
	}

	public synchronized void onPause() {
		cancel();
	}

	public synchronized void onResume() {
		onActivity();
	}

	private synchronized void cancel() {
		AsyncTask<?, ?, ?> task = inactivityTask;
		if (task != null) {
			task.cancel(true);
			inactivityTask = null;
		}
	}

	public void shutdown() {
		cancel();
	}

	private class InactivityAsyncTask extends AsyncTask<Object, Object, Object> {
		@Override
		protected Object doInBackground(Object... objects) {
			try {
				Thread.sleep(INACTIVITY_DELAY_MS);
				
//				EventItem item = new EventItem(EventItem.ID_AUTO_CLOSE_QR);
//				EventBus.getDefault().post(item);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

}
