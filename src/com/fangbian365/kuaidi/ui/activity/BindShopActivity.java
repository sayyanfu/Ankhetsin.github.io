package com.fangbian365.kuaidi.ui.activity;

import java.util.List;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.RootPwd;
import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.ui.fragment.LoginFragment;
import com.fangbian365.kuaidi.ui.fragment.qrcode.CaptureFragment;
import com.fangbian365.kuaidi.ui.uisupport.ToastManualUpdate;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 绑定店铺
 * @author anqi
 */
public class BindShopActivity extends FragmentActivity {
	private static final String TAG = "BindShopActivity";
	private EditText mDevicePwdEt;
	private TextView mBindshopTv;
	private TextView mDeviceNumTv; //设备号
	public static ViewGroup mFrameViews = null;
	public ViewGroup mLayout;
	
	private RelativeLayout mLayoutLoading;
	private ImageView mIvLoading;
	private Animation hyperspaceJumpAnimation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_shop);
		mFrameViews = (ViewGroup) findViewById(R.id.main_frame);
		mLayoutLoading = (RelativeLayout) findViewById(R.id.layout_loading);
		mLayoutLoading.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		mIvLoading = (ImageView) findViewById(R.id.iv_loading);
		hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.loading_animation);  
		
		
		try {
			PrefsConfig.loadScanQR(BindShopActivity.this);
			if (PrefsConfig.isScanQR) {
				FrameLog.d(TAG, "---------" + PrefsConfig.isScanQR);
				LoginFragment loginFragment = new LoginFragment();
				Bundle arg = new Bundle();
				arg.putString(Constans.SHOP_ID, PrefsConfig.ShopId);
				loginFragment.setArguments(arg);
				entrySubFragment(loginFragment);
				return;
			}else {}
		} catch (Exception e) {
			e.printStackTrace();
			FrameLog.e(TAG, e);
		}
		
		mLayout = (RelativeLayout)findViewById(R.id.bg_merchant);
		mDevicePwdEt = (EditText) findViewById(R.id.bindshop_et);
		mDevicePwdEt.setFocusable(true);
		mDevicePwdEt.setFocusableInTouchMode(true);
		mDevicePwdEt.requestFocus();
		mBindshopTv = (TextView) findViewById(R.id.bindshop_tv);
		mBindshopTv.setOnClickListener(mBindshopListener);
		mDeviceNumTv = (TextView) findViewById(R.id.bindshop_tv_tottom);
		if (!TextUtils.isEmpty(PrefsConfig.devNum)) {
			mDeviceNumTv.setText("手机点餐  "+ new String(Base64.decode(PrefsConfig.devPwd, Base64.DEFAULT)));
		}		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
	public void showLoading(boolean isShow) {
		if(isShow) {
			mLayoutLoading.setVisibility(View.VISIBLE);
			mIvLoading.startAnimation(hyperspaceJumpAnimation);
		} else {
			mIvLoading.clearAnimation();
			mLayoutLoading.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		
		
		Intent intent = getIntent();
		if (intent != null) {
			String msg = intent.getStringExtra(Constans.LOGOUT);
			if (!TextUtils.isEmpty(msg) && msg.equals("logout")) {
				LoginFragment fragment = new LoginFragment();
				entrySubFragment(fragment);
			}
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	private OnClickListener mBindshopListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			SysWorkTools.closeKeyboard(mDevicePwdEt);
			
			String shopEt = mDevicePwdEt.getText().toString().trim();
			if (TextUtils.isEmpty(shopEt)) {
				
				showToast(getString(R.string.bindshop_input_hint));
				return;
			}
			if (!TextUtils.isEmpty(PrefsConfig.devPwd)) { //设置设备号后
				if (shopEt.equals(new String(Base64.decode(PrefsConfig.devPwd, Base64.DEFAULT)))) {
					CaptureFragment captureFragment = new CaptureFragment();
					entrySubFragment(captureFragment);
					return;
				}else {
					showToast("设备密码错误");
					return;
				}
			}else { //未设置设备号
				if (shopEt.equals(Constans.INIT_DEV_NUM)) {
					RootPwd pwd = new RootPwd();
					String base64PWD = Base64.encodeToString(shopEt.getBytes(), Base64.DEFAULT);
					pwd.setRootPwd(base64PWD);
					PrefsConfig.saveRootPwd(pwd);
					CaptureFragment captureFragment = new CaptureFragment();
					entrySubFragment(captureFragment);
					return;
				}else {
					showToast("设备密码错误");
					return;
				}
				
			}
			
		}
	};
	
	public void entrySubFragment(Fragment fragment) {

		if (fragment != null) {
			String tag = fragment.getClass().getSimpleName();
			FrameLog.d(TAG, "jumpTo:" + tag);
			mFrameViews.setVisibility(View.VISIBLE);
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.add(R.id.main_frame, fragment, tag);
			transaction.addToBackStack(tag); // 添加回退栈
			transaction.commitAllowingStateLoss();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			SysWorkTools.closeKeyboard(mDevicePwdEt);
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		FragmentManager fragmentManager = getSupportFragmentManager();

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			int count = fragmentManager.getBackStackEntryCount();
			if (count > 0) {
				List<Fragment> fList = fragmentManager.getFragments();
				Fragment f = null;
				for (int i = fList.size(); i > 0; i--) {
					if (fList.get(i - 1) != null) {
						f = fList.get(i - 1);
						break;
					}
				}
				if (f instanceof LoginFragment) {
					boolean neetTip = ((LoginFragment) f).onKeyDown(keyCode);
					if (neetTip)
						return true;
				}

				fragmentManager.popBackStack();
				return true;
			} else {
				moveTaskToBack(true);
			}

			break;
		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}
	/**
	 * 数据更新成功Toast
	 */
	public void showToast(String str){
		ToastManualUpdate.createToastConfig().Show(BindShopActivity.this, false, (ViewGroup)findViewById(R.id.toast_layout_root), 0, str);
	}
	
	/**
	 * 数据更新成功Toast
	 */
	public void showGetDateSucceedToast(String str){
		ToastManualUpdate.createToastConfig().Show(BindShopActivity.this, true, (ViewGroup)findViewById(R.id.toast_layout_root), R.drawable.bg_ok_manual_update, str);
	}
	
	/**
	 * 数据更新失败Toast
	 */
	public void showGetDateFailedToast(String str){
		ToastManualUpdate.createToastConfig().Show(BindShopActivity.this, true, (ViewGroup)findViewById(R.id.toast_layout_root), R.drawable.bg_cancel_manual_update, str);
	}
	
}
