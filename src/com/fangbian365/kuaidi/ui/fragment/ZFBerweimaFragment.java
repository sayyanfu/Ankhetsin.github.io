package com.fangbian365.kuaidi.ui.fragment;

import java.util.Hashtable;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cardinfolink.cashiersdk.listener.CashierListener;
import com.cardinfolink.cashiersdk.model.OrderData;
import com.cardinfolink.cashiersdk.model.ResultData;
import com.cardinfolink.cashiersdk.sdk.CashierSdk;
import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.HaveProductBean;
import com.fangbian365.kuaidi.base.bean.PayTypeBean;
import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.PrefsUtils;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.mod.asynctask.http.FetchListener;
import com.fangbian365.kuaidi.mod.manager.DataFetchManager;
import com.fangbian365.kuaidi.ui.activity.MainActivity;
import com.fangbian365.kuaidi.ui.receiver.HomeBroadcastReceiver;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class ZFBerweimaFragment extends HeaderFragment {

	private Bundle bundle;
	private ImageView iv_erweima;
	private String danNo;
	private String ysje = "";
	private String lsh;
	private String tag = "ZFBerweimaFragment";
	private JSONArray payData = new JSONArray();
	private List<HaveProductBean> hList;
	private String mling;
	private String ysje02;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bundle = getArguments();
		if (bundle != null) {
			danNo = bundle.getString("danNo");
			ysje = bundle.getString("money");
			lsh = bundle.getString("lsh");
			hList = (List<HaveProductBean>) bundle.getSerializable("list");
			mling = bundle.getString("ml");
		}
		FrameLog.e(tag, danNo + "----------------------");
		PrefsUtils.savePrefString(getActivity(), lsh + "dz", danNo);
		ysje02 = PrefsUtils.loadPrefString(getActivity(), lsh + "zm");
		zfbPreparePay(true);
		
		if ((null != ysje02
				&& !ysje02.equals(ysje))
				|| null == PrefsUtils.loadPrefString(getActivity(), lsh + "z")
				|| PrefsUtils.loadPrefString(getActivity(), lsh + "z").equals("")) {
			//zfbPreparePay(true);
		} else {
			mZfbQrCode = PrefsUtils.loadPrefString(getActivity(), lsh + "z");
			FrameLog.e(tag, "支付宝二维码*******" + mZfbQrCode);
			thread.start();
		}

		if (null == ysje02) {
			ysje02 = "";
		}
		if (null != ysje02 || !ysje02.equals(ysje)) {
			PrefsUtils.savePrefString(getActivity(), lsh + "zm", ysje);
		}
		
		FrameLog.e(tag, ysje + "**************" + ysje02);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.erweima, null);
		iv_erweima = (ImageView) view.findViewById(R.id.iv_erweima);
		return super.onCreateView(inflater, view, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		iv_erweima.setImageBitmap(createQRImage(mZfbQrCode, 200, 200));
	}

	/**
	 * 生成二维码 要转换的地址或字符串,可以是中文
	 * 
	 * @param url
	 * @param width
	 * @param height
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Bitmap createQRImage(String url, final int width,
			final int height) {
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, width, height, hints);
			int[] pixels = new int[width * height];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else {
						pixels[y * width + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	String mZfbQrCode;

	private void zfbPreparePay(final boolean bIsFirst) {
		OrderData orderData = new OrderData();
		orderData.chcd = "ALP";
		orderData.currency = "156";
		orderData.txamt = ysje;
		orderData.orderNum = String.valueOf(danNo);
		CashierSdk.startPrePay(orderData, new CashierListener() {

			@Override
			public void onResult(ResultData result) {
				if (result.busicd.equals("PAUT")) {// 预下单

					mZfbQrCode = result.qrcode;
					Log.d("MainActivity", "支付宝二维码****  " + mZfbQrCode);
					PrefsUtils.savePrefString(getActivity(), lsh + "z",
							mZfbQrCode);
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
					thread.start();
				}
			}

			@Override
			public void onError(int arg0) {
				if (arg0 == 0) {
					zfbPreparePay(true);
				}
				FrameLog.e(tag, arg0+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			}
		});
	}

	boolean isRun = true;
	Thread thread = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {

				while (isRun) {
					searchZfResult();
					Thread.sleep(6000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	});

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0) {
				iv_erweima.setImageBitmap(createQRImage(mZfbQrCode, 200, 200));
			}
		}
	};

	private void searchZfResult() {
		// TODO Auto-generated method stub
		OrderData od = new OrderData();
		od.chcd = "WXP";
		od.currency = "156";
		od.txamt = ysje;
		od.orderNum = danNo;
		od.origOrderNum = danNo;
		CashierSdk.startQy(od, new CashierListener() {

			@Override
			public void onResult(ResultData arg0) {
				// TODO Auto-generated method stub

				if (arg0.busicd.equals("INQY")) {

					FrameLog.e(tag, "检测支付是否完成------------" + arg0.respcd);
					if (arg0.respcd.equals("00") || arg0.respcd == "00") {
						isRun = false;
						doBill();
						FrameLog.e(tag, "付款成功");
						PrefsUtils.clearPreByKey(getActivity(), lsh + "z");
						PrefsUtils.clearPreByKey(getActivity(), lsh + "zm");
						PrefsUtils.clearPreByKey(getActivity(), lsh + "dz");
					}
				}

			}

			@Override
			public void onError(int arg0) {
				// TODO Auto-generated method stub
				FrameLog.e(tag, "errorcode" + arg0);
			}
		});
	}

	/**
	 * 支付方式
	 * 
	 * @param money
	 */
	private void appendPatData() {
		payData = new JSONArray();
		PayTypeBean payItem = new PayTypeBean();
		payItem.setCode("zfb");
		payItem.setCodename("支付宝");
		payItem.setMoney(ysje);
		payItem.setRemark1("");
		payItem.setRemark2("");
		FrameLog.e(tag, payItem.toString());
		payData.add(payItem);

	}

	/**
	 * 结账
	 */
	private void doBill() {
		// TODO Auto-generated method stub
		appendPatData();
		JSONArray arr = new JSONArray();
		for (HaveProductBean item : hList) {
			JSONObject obj = new JSONObject();
			obj.put("id", item.getpId());
			obj.put("dis", 1.0f);
			arr.add(obj);
		}
		DataFetchManager.getInstance().fetchFinalBill(false, lsh, "1",
				arr.toJSONString(), payData.toJSONString(), "", "",
				Constans.DF.format(Double.parseDouble(mling)), "0",
				PrefsConfig.workerName, "0", PrefsConfig.workerName,
				new FetchListener() {

					@Override
					public void onPreFetch() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPostFetch(int status, Object... result) {
						// TODO Auto-generated method stub
						if (status == FetchListener.STATUS_OK) {
							SysWorkTools.showToast(getActivity(), "结账成功");

							getActivity().getSupportFragmentManager()
									.popBackStack();
							((MainActivity) getActivity()).doFetchTableInfo();
							HomeBroadcastReceiver.onDataTransfer(
									Constans.TYPE_BILL_DOWN, true);

						} else {
							String msg = (String) result[0];
							SysWorkTools.showToast(getActivity(), msg);
						}
					}
				});
	}

	@Override
	protected String getTitle() {
		// TODO Auto-generated method stub
		return "支付宝二维码";
	}

	@Override
	protected int getMenuResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean onKeyDown(int keyCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isRun = false;

	}

}
