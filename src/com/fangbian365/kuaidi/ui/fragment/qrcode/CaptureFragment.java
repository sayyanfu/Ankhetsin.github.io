package com.fangbian365.kuaidi.ui.fragment.qrcode;

import java.io.IOException;
import java.lang.reflect.Field;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.CHBApp;
import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.mod.qrcode.camera.CameraManager;
import com.fangbian365.kuaidi.mod.qrcode.decode.DecodeThread;
import com.fangbian365.kuaidi.mod.qrcode.utils.BeepManager;
import com.fangbian365.kuaidi.mod.qrcode.utils.CaptureActivityHandler;
import com.fangbian365.kuaidi.mod.qrcode.utils.InactivityTimer;
import com.fangbian365.kuaidi.ui.activity.BindShopActivity;
import com.fangbian365.kuaidi.ui.fragment.HeaderFragment;
import com.fangbian365.kuaidi.ui.fragment.LoginFragment;
import com.google.zxing.Result;
/**
 * @author anqi
 * @since 2015/6/22
 * @version 1.0
 */
public class CaptureFragment extends HeaderFragment implements SurfaceHolder.Callback{
	
	private String mShopId;
	private String mKey;
	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;

	private SurfaceView scanPreview = null;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	private ImageView scanLine;

	private Rect mCropRect = null;
	private boolean isHasSurface = false;
	
	private TranslateAnimation animation;
	
	private static final String TAG = "CaptureFragment";
	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Window window = getActivity().getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup contentView = (ViewGroup) inflater.inflate(R.layout.capture_layout, null);
		
		scanPreview = (SurfaceView) contentView.findViewById(R.id.capture_preview);
		scanContainer = (RelativeLayout) contentView.findViewById(R.id.capture_container);
		scanCropView = (RelativeLayout) contentView.findViewById(R.id.capture_crop_view);
		scanLine = (ImageView) contentView.findViewById(R.id.capture_scan_line);
		
		inactivityTimer = new InactivityTimer();
		beepManager = new BeepManager();
		
		animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.9f);
		
		animation.setDuration(2000);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.RESTART);
		return super.onCreateView(inflater, contentView, savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	protected int getMenuResId() {
		return -1;
	}
	@Override
	protected String getTitle() {
		return getString(R.string.QR_Code);
	}

	@Override
	public void onResume() {
		super.onResume();
		scanLine.startAnimation(animation);
		scanLine.setVisibility(View.VISIBLE);
		// CameraManager must be initialized here, not in onCreate(). This is
		// necessary because we don't
		// want to open the camera driver and measure the screen size if we're
		// going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the
		// wrong size and partially
		// off screen.
		cameraManager = new CameraManager(CHBApp.getAppContext());

		handler = null;

		if (isHasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(scanPreview.getHolder());
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			scanPreview.getHolder().addCallback(this);
		}

		inactivityTimer.onResume();
	}
	
	@Override
	public void onPause() {
		scanLine.clearAnimation();
		scanLine.setVisibility(View.GONE);
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		cameraManager.closeDriver();
		if (!isHasSurface) {
			scanPreview.getHolder().removeCallback(this);
		}
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		inactivityTimer.shutdown();
		beepManager.close();
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		if (holder == null) {
			FrameLog.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!isHasSurface) {
			isHasSurface = true;
			CHBApp.getInstance().getMainThreadHandler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					initCamera(holder);
				}
			}, 1000);
		}
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isHasSurface = false;
	}
	
	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 * @param rawResult
	 *            The contents of the barcode.
	 * 
	 * @param bundle
	 *            The extras
	 */
	public void handleDecode(Result rawResult, Bundle bundle) {
		beepManager.playBeepSoundAndVibrate();
		onPause();
		FrameLog.v(TAG, "meg---> " + rawResult.getText());
		if (parseQR(rawResult.getText())) {
			((BindShopActivity)getActivity()).showToast("扫描成功！");
			PrefsConfig.saveScan(true);
			LoginFragment loginFragment = new LoginFragment();
			PrefsConfig.saveShopId(mShopId);
			close();
			((BindShopActivity)getActivity()).entrySubFragment(loginFragment);
		}else {
			((BindShopActivity)getActivity()).showToast("扫描失败！");
			PrefsConfig.saveScan(false);
		}
		//final CustomerInfo customerInfo = parseQRToCustomer(rawResult.getText());
//		if (customerInfo == null) {
//			SysWorkTools.showToast(getActivity(), "验证失败，请重新扫描");
//			onResume();
//			return;
//		}
		
		// TODO 这里处理二维码扫描结果
		
	}
	
	private boolean parseQR(String result){
		
		try {
			if (!TextUtils.isEmpty(result)) {
				JSONObject obj = JSONObject.parseObject(result);
				mShopId = obj.getString("shopId");
				mKey = obj.getString("key");
				String str = "chb365";
				if (SysWorkTools.md5(str + mShopId).equals(mKey)) {
					return true;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			FrameLog.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
			}
			initCrop();
		} catch (IOException ioe) {
			FrameLog.e(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			FrameLog.e(TAG, e);
			displayFrameworkBugMessageAndExit();
		}
	}
	
	private void displayFrameworkBugMessageAndExit() {
		// camera error
	/*	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage("相机打开出错，请稍后重试");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				close();
			}

		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				close();
			}
		});
		builder.show();*/
		CommDialogFragment commDialog = new CommDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString(Constans.KEY_ERR_CAMERA, "相机打开出错，请稍后重试");
		bundle.putInt(CommDialogFragment.KEY_DIALOG_TYPE, 1);
		commDialog.setArguments(bundle);
		commDialog.show(getChildFragmentManager(), "commDialog");
	}
	
	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = cameraManager.getCameraResolution().y;
		int cameraHeight = cameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}
	
	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Rect getCropRect() {
		return mCropRect;
	}
	
	@Override
	public boolean onKeyDown(int keyCode) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			close();
			return true;
		default:
			return false;
		}
	}
}
