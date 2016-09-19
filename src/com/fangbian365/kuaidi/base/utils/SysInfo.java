package com.fangbian365.kuaidi.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.mod.dirmanager.CHBDirContext;
import com.fangbian365.kuaidi.mod.dirmanager.DirectoryManager;

public class SysInfo {

	private static String TAG = "SysInfo";
	public static final String APP_NAME = "Chihuobao";

	public static String DEVICE_IMEI;

	public static String DEVICE_MAC_ADDR;

	public static int VERSION_CODE;

	public static String VERSION_NAME;
	
	public static int WIDTH;

	public static int HEIGHT;

	public static float DENSITY;

	public static int DENSITY_DPI;

	public static float SCALED_DENSITY;
	
	public static int IMAG_CACHE_SIZE = 0;

	private static boolean inited = false;
	private static DirectoryManager dirManager = null;

	public synchronized static boolean init(Activity context) {

		if (inited) {
			return true;
		}

		try {
			if (!initFileSystem()) {
				return false;
			}
			initScreenInfo(context);
			String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
			String macAddr = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();

			DEVICE_IMEI = imei;
			DEVICE_MAC_ADDR = macAddr;
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			VERSION_CODE = pi.versionCode;
			VERSION_NAME = pi.versionName;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		inited = true;
		return true;
	}

	public synchronized static boolean initFileSystem() {
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			FrameLog.v(TAG, "initFileSystem:" + Environment.getExternalStorageState());
			
			return false;
		}
		if (dirManager == null)
			dirManager = new DirectoryManager(new CHBDirContext(APP_NAME));
		
		return dirManager.createAll();
	}
	
	public static void initScreenInfo(Activity activity) {
		if (activity == null) return;
		if (WIDTH == 0) {
			try {
				DisplayMetrics dm = new DisplayMetrics();
				activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
				WIDTH = Math.min(dm.widthPixels, dm.heightPixels);
				HEIGHT = Math.max(dm.widthPixels, dm.heightPixels);
				DENSITY = dm.density;
				
				DENSITY_DPI = dm.densityDpi;
				SCALED_DENSITY = dm.scaledDensity;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static DirectoryManager getDirManager() {
		if (dirManager == null)
			initFileSystem();
		return dirManager;
	}
}