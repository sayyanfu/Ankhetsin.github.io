package com.fangbian365.kuaidi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.CHBApp;
import com.fangbian365.kuaidi.base.utils.CHBThreadPool;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.CHBThreadPool.JobType;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

public class WelcomeActivity extends Activity {
	
private static String TAG = "WelcomeActivity";
	
	public static boolean isInited = false;
	private ImageView welcomeView;
	private Intent mIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		MobclickAgent.updateOnlineConfig(WelcomeActivity.this);
		MobclickAgent.openActivityDurationTrack(false);
		AnalyticsConfig.enableEncrypt(true);
		getWindow().setBackgroundDrawable(null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		if(!isInited) {
			welcomeView = new ImageView(this);
			welcomeView.setBackgroundResource(R.drawable.welcome);
//			welcomeView.setBackgroundDrawable(getResources().getDrawable(R.drawable.welcome));
			welcomeView.setScaleType(ScaleType.FIT_XY);
			setContentView(welcomeView);
			doInit();
		} else {
			msgSucc();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	/**
	 * 初始化环境
	 */
	private void doInit(){
		CHBThreadPool.runThread(JobType.NORMAL, new Runnable() {
			
			@Override
			public void run() {
				try{
					Thread.sleep(2000);
					
					CHBApp.getInstance().initApp(WelcomeActivity.this);
//					//初始化文件系统
//					boolean isSuccess = AppContext.initFileSystem();
//					if(!isSuccess){
//						msgNoSdCard();
//			            return;
//					}
					// 文件系统初始化失败
			        if (CHBApp.getInstance().mInitFSFailed) {
			        	msgNoSdCard();
			            return;
			        }
					msgSucc();

				}catch(Throwable e){
					msgExit();
				}
			}
		});
	}
	
	
	private void msgSucc() {
		if (PrefsConfig.login_status) {
			// 已登录用户直接进入主界面
			mIntent = new Intent(this, MainActivity.class);
		} else {
			// 进入登录注册界面
			mIntent = new Intent(this, BindShopActivity.class);
		}

		transmitActivityParams(mIntent);

		CHBApp.getInstance().getMainThreadHandler().post(new Runnable() {
			@Override
			public void run() {
				switchScene(mIntent);
			}
		});

		isInited = true;
	}
	
	
	private void transmitActivityParams(Intent newIntent) {
		Intent intent = getIntent();
//		Uri uri = intent.getData();
//		if (uri != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
//			newIntent.putExtra(PLAY_LOCAL_SWITCH, true);
//			newIntent.putExtra(PLAY_LOCAL_URI, uri.toString());
//		}
//		
//		newIntent.putExtra(SHARE_START_PIC, isShare);
		// 柳瑞军：Copy all extras in 'src' in to // this intent.
		// 所有通过putExtra传递的值，都会被传递
		newIntent.putExtras(intent);
	}
	
	private void msgNoSdCard() {
		CHBApp.getInstance().getMainThreadHandler().post(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(WelcomeActivity.this, NoSdcardActivity.class));
	            finish();
			}
		});
	}
	
	private void msgExit() {
		CHBApp.getInstance().getMainThreadHandler().post(new Runnable() {
			@Override
			public void run() {
				finish();
			}
		});
	}
	
	
	/**
	 * 场景切换
	 */
	private void switchScene(Intent intent) {
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_left);
	}

}
