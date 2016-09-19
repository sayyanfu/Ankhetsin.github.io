package com.fangbian365.kuaidi.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.CHBApp;
import com.umeng.analytics.MobclickAgent;

public class NoSdcardActivity extends Activity {
	private BroadcastReceiver mSdcardReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
                CHBApp.getInstance().initApp(NoSdcardActivity.this);
                if (!CHBApp.getInstance().mInitFSFailed) {
                    startActivity(new Intent(NoSdcardActivity.this, MainActivity.class));
                    mKillProcess = false;
                    finish();
                }
            }
        }
    };
    

    private boolean mKillProcess = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sdcard_watcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
    	MobclickAgent.onResume(this);

        IntentFilter intentFilter = new IntentFilter(
                Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addDataScheme("file");
        registerReceiver(mSdcardReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    	MobclickAgent.onPause(this);
        unregisterReceiver(mSdcardReceiver);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mKillProcess)
            android.os.Process.killProcess(android.os.Process.myPid());
    }
}
