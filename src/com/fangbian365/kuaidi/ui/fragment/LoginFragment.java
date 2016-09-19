package com.fangbian365.kuaidi.ui.fragment;

import org.xutils.DbManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.CHBApp;
import com.fangbian365.kuaidi.base.bean.BusinessDetail;
import com.fangbian365.kuaidi.base.bean.DeviceNum;
import com.fangbian365.kuaidi.base.bean.DevicePwd;
import com.fangbian365.kuaidi.base.bean.RootPwd;
import com.fangbian365.kuaidi.base.bean.UserInfo;
import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.mod.asynctask.http.FetchListener;
import com.fangbian365.kuaidi.mod.manager.DataFetchManager;
import com.fangbian365.kuaidi.ui.activity.BindShopActivity;
import com.fangbian365.kuaidi.ui.activity.MainActivity;
import com.fangbian365.kuaidi.ui.fragment.qrcode.CaptureFragment;
import com.fangbian365.kuaidi.ui.uisupport.CircleImageView;
import com.fangbian365.kuaidi.ui.uisupport.SettingPop;
import com.fangbian365.kuaidi.ui.uisupport.SettingPop.ITypeSetListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 登录界面
 */
public class LoginFragment extends Fragment implements ITypeSetListener, OnClickListener{

	private static final String TAG = "LoginFragment";
	//private ImageView mSettingIv;
	private CircleImageView mHeadLogo;
	private EditText mWorkNumEt;
	private EditText mPwdEt;
	private TextView mLoginTv;
	private TextView mDeviceNumTv; // 设备号
	private SettingPop mPopu;
	private LinearLayout mLoginLayout;
	private LinearLayout mAuthLayout;
	private EditText mAuthEt;
	private TextView mAuthTv;
	private TextView mTitleTv; // 店铺名称
	private int mType = 0;
	private DbManager dbManager;
	private RelativeLayout mRelative;
	private TextView agin_tv, help_tv;//重新绑定店铺 使用帮助
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbManager = CHBApp.getInstance().getDbManager();
		if (SysWorkTools.isNetAvailable()) {
			doFetchBackFood();
			doFetchMerchantDetail();
		} else {
			SysWorkTools.showToast(getActivity(), "请检查网络");
		}

	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup contentView = (ViewGroup) inflater.inflate(
				R.layout.fragment_login, null);
		agin_tv = (TextView) contentView.findViewById(R.id.login_agin_TV);
		agin_tv.setOnClickListener(this);
		help_tv = (TextView) contentView.findViewById(R.id.login_help_TV);
		help_tv.setOnClickListener(this);
		mRelative = (RelativeLayout) contentView.findViewById(R.id.bg_merchant);
		mLoginLayout = (LinearLayout) contentView.findViewById(R.id.login_layout);
		mAuthLayout = (LinearLayout) contentView.findViewById(R.id.auth_layout);
		mAuthEt = (EditText) contentView.findViewById(R.id.auth_et);
		mTitleTv = (TextView) contentView.findViewById(R.id.login_title_tv);
		mAuthTv = (TextView) contentView.findViewById(R.id.auth_btn_tv);
		mAuthTv.setOnClickListener(authListener);
//		mSettingIv = (ImageView) contentView
//				.findViewById(R.id.login_setting_IV);
//		mSettingIv.setOnClickListener(showSettingListener);
		mHeadLogo = (CircleImageView) contentView
				.findViewById(R.id.login_head_IV);
		mWorkNumEt = (EditText) contentView.findViewById(R.id.login_worknum_et);
		mPwdEt = (EditText) contentView.findViewById(R.id.login_pwd_et);
		mWorkNumEt.setOnEditorActionListener(mWrokEtActionListener);
		mPwdEt.setOnEditorActionListener(mPwdEtActionListener);
		mLoginTv = (TextView) contentView.findViewById(R.id.login_btn_tv);
		mLoginTv.setOnClickListener(loginListener);
		mDeviceNumTv = (TextView) contentView
				.findViewById(R.id.login_tv_tottom);
		mPopu = new SettingPop(getActivity(), SettingPop.VERTICAL);
		mPopu.seTypeListener(this);
		mPopu.setOnDismissListener(new SettingPop.OnDismissListener() {
			@Override
			public void onDismiss() {
			}
		});

		if (!TextUtils.isEmpty(new String(Base64.decode(PrefsConfig.devPwd,
				Base64.DEFAULT)))) {
			mDeviceNumTv.setText("手机点餐  "
					+ new String(Base64.decode(PrefsConfig.devPwd,
							Base64.DEFAULT)));
		}
		return contentView;
	}

	private OnClickListener showSettingListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mPopu.show(v);
		}
	};

	private OnClickListener authListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			String authStr = mAuthEt.getText().toString().trim();
			switch (mType) {
			case 0: // 修改设备密码
				if (TextUtils.isEmpty(authStr)) {
					((BindShopActivity) getActivity()).showToast("请输入新设备密码！");
					return;
				} else {
					String base64PWD = Base64.encodeToString(
							authStr.getBytes(), Base64.DEFAULT);
					DevicePwd pwd = new DevicePwd();
					pwd.setDevPwd(base64PWD);
					PrefsConfig.saveDevPwd(pwd);
					((BindShopActivity) getActivity())
							.showGetDateSucceedToast("设备密码修改成功！");
					mLoginLayout.setVisibility(View.VISIBLE);
					mAuthLayout.setVisibility(View.GONE);
					requireFocus(mAuthEt, false);
					requireFocus(mPwdEt, true);
					requireFocus(mWorkNumEt, true);
					SysWorkTools.closeKeyboard(mWorkNumEt);
				}
				break;
			case 1: // 修改设备号
				if (TextUtils.isEmpty(authStr)) {
					((BindShopActivity) getActivity()).showToast("请输入新设备编号！");
					return;
				} else {
					mDeviceNumTv.setText("手机点餐  " + authStr);
					String base64Num = Base64.encodeToString(
							authStr.getBytes(), Base64.DEFAULT);
					DeviceNum num = new DeviceNum();
					num.setDevNum(base64Num);
					PrefsConfig.saveDevNum(num);
					((BindShopActivity) getActivity())
							.showGetDateSucceedToast("设备编号修改成功！");
					mAuthEt.setText("");
					mLoginLayout.setVisibility(View.VISIBLE);
					mAuthLayout.setVisibility(View.GONE);
					requireFocus(mAuthEt, false);
					requireFocus(mPwdEt, true);
					requireFocus(mWorkNumEt, true);
					SysWorkTools.closeKeyboard(mWorkNumEt);
				}
				break;
			case 2: // 设备密码确认
				if (TextUtils.isEmpty(authStr)) {
					((BindShopActivity) getActivity()).showToast("请输入设备密码！");
					return;
				} else {
					if (authStr.equals(Constans.INIT_DEV_NUM)) {
						RootPwd pwd = new RootPwd();
						String base64PWD = Base64.encodeToString(
								authStr.getBytes(), Base64.DEFAULT);
						pwd.setRootPwd(base64PWD);
						PrefsConfig.saveRootPwd(pwd);
						((BindShopActivity) getActivity())
								.showGetDateSucceedToast("设备密码验证成功！");
						mAuthEt.setText("");
						PrefsConfig.confirmDevPwd();
						mLoginLayout.setVisibility(View.VISIBLE);
						mAuthLayout.setVisibility(View.GONE);
						requireFocus(mAuthEt, false);
						requireFocus(mPwdEt, true);
						requireFocus(mWorkNumEt, true);
						SysWorkTools.closeKeyboard(mWorkNumEt);
						return;
					} else {
						((BindShopActivity) getActivity())
								.showGetDateFailedToast("设备密码错误！");
						return;
					}
				}
				// break;
			default:
				break;
			}
		}
	};

	private OnClickListener loginListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			doLoginPress();
		}
	};

	private void requireFocus(EditText et, boolean isFocusable) {
		et.setFocusable(isFocusable);
		et.setFocusableInTouchMode(isFocusable);
		et.requestFocus();
	}

	private void doLogin(String worknum, String pwd) {
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fetchLogin(worknum, pwd,
					new FetchListener() {

						@Override
						public void onPreFetch() {
							((BindShopActivity) getActivity())
									.showLoading(true);
						}

						@Override
						public void onPostFetch(int status, Object... result) {
							((BindShopActivity) getActivity()).showLoading(false);
							if (status == FetchListener.STATUS_OK) {
								UserInfo user = (UserInfo) result[0];
								FrameLog.d("AAAA", "111111" + user.getWorkerName());
								PrefsConfig.saveUser(user);
								FrameLog.d("AAAA", "3333333" + PrefsConfig.workerName);
								doDisappear();

							} else {
								String msg = (String) result[0];
								((BindShopActivity) getActivity()).showGetDateFailedToast(msg);
								setLoginFalseState();
							}
						}
					});
		} else {
			SysWorkTools.showToast(getActivity(), "请检查网络");
		}

	}

	private void doFetchMerchantDetail() {
		DataFetchManager.getInstance().businessDetail(new FetchListener() {

			@Override
			public void onPreFetch() {
				// ProgressView.getInstance(getActivity()).showProgress(mLayout);
			}

			@Override
			public void onPostFetch(int status, Object... result) {
				// ProgressView.getInstance(getActivity()).hideProgress(mLayout);
				if (status == FetchListener.STATUS_OK) {
					BusinessDetail detail = (BusinessDetail) result[0];
					PrefsConfig.saveBusinessDetail(detail);
					mTitleTv.setText(detail.getShopName());
				} else {
				}
			}
		});

	}

	private void doDisappear() {
		Intent intent = new Intent(getActivity(), MainActivity.class);
		startActivity(intent);
		getActivity().finish();
	}

	private void setLoginFalseState() {
		// mWorkNumEt.setText("");
		// mPwdEt.setText("");
		mWorkNumEt.setFocusable(true);
		mWorkNumEt.setFocusableInTouchMode(true);
		mWorkNumEt.requestFocus();
	}

	@Override
	public void setMenuType(int num) {
		switch (num) {
		case 0: // 重新绑定店铺
			//mPopu.dismiss();
			
			break;
		case 1: // 修改设备密码

			mPopu.dismiss();
			if (!PrefsConfig.isCheckDevPwd) {
				confirmDevPwd();
			} else {
				mLoginLayout.setVisibility(View.GONE);
				mAuthLayout.setVisibility(View.VISIBLE);
				requireFocus(mAuthEt, true);
				requireFocus(mWorkNumEt, false);
				requireFocus(mPwdEt, false);
				mType = 0;
				mAuthEt.setText("");
				mAuthEt.setHint("请输入新设备密码");
				mAuthTv.setText("修改设备密码");
			}
			break;
		case 2: // 修改设备号

			mPopu.dismiss();
			if (!PrefsConfig.isCheckDevPwd) {
				confirmDevPwd();
			} else {
				mLoginLayout.setVisibility(View.GONE);
				mAuthLayout.setVisibility(View.VISIBLE);
				requireFocus(mAuthEt, true);
				requireFocus(mWorkNumEt, false);
				requireFocus(mPwdEt, false);
				mType = 1;
				mAuthEt.setText("");
				mAuthEt.setHint("请输入新设备编号");
				mAuthTv.setText("修改设备编号");
			}
			break;
		case 3: // 手动更新数据
			mPopu.dismiss();
			mLoginLayout.setVisibility(View.VISIBLE);
			mAuthLayout.setVisibility(View.GONE);
			requireFocus(mAuthEt, false);
			requireFocus(mPwdEt, true);
			requireFocus(mWorkNumEt, true);
			doFetchBackFood();
			break;
		default:
			break;
		}
	}

	private void confirmDevPwd() {
		mLoginLayout.setVisibility(View.GONE);
		mAuthLayout.setVisibility(View.VISIBLE);
		requireFocus(mAuthEt, true);
		requireFocus(mPwdEt, false);
		requireFocus(mWorkNumEt, false);
		mType = 2;
		mAuthEt.setHint("请输入设备密码");
		mAuthTv.setText("确定");
	}

	/**
	 * 退菜说明
	 */
	private void doFetchBackFood() {
		DataFetchManager.getInstance().fetchBackFood(new FetchListener() {

			@Override
			public void onPreFetch() {
			}

			@Override
			public void onPostFetch(int status, Object... result) {
				if (status == FetchListener.STATUS_OK) {
					doFetchGiveFoodReason();
				} else {
					if ((BindShopActivity) getActivity() != null) {
						((BindShopActivity) getActivity())
								.showGetDateFailedToast("数据更新失败！");
					}
					return;
				}

			}
		});
	}

	/**
	 * 赠菜原因
	 */
	private void doFetchGiveFoodReason() {
		DataFetchManager.getInstance().fetchGiveFoodReason(new FetchListener() {

			@Override
			public void onPreFetch() {
			}

			@Override
			public void onPostFetch(int status, Object... result) {
				if (status == FetchListener.STATUS_OK) {
					doFetchTaste();
				} else {
					if ((BindShopActivity) getActivity() != null) {
						((BindShopActivity) getActivity())
								.showGetDateFailedToast("数据更新失败！");
					}
					return;
				}
			}
		});
	}

	/**
	 * 口味信息同步
	 */
	private void doFetchTaste() {

		DataFetchManager.getInstance().fetchTaste(new FetchListener() {

			@Override
			public void onPreFetch() {
			}

			@Override
			public void onPostFetch(int status, Object... result) {
				if (status == FetchListener.STATUS_OK) {
					doFetchProductInfo();
				} else {
					if ((BindShopActivity) getActivity() != null) {
						((BindShopActivity) getActivity())
								.showGetDateFailedToast("数据更新失败！");
					}
					return;
				}
			}
		});
	}

	/**
	 * 菜品信息获取
	 */
	private void doFetchProductInfo() {
		DataFetchManager.getInstance().fetchProductInfo(new FetchListener() {

			@Override
			public void onPreFetch() {
			}

			@Override
			public void onPostFetch(int status, Object... result) {
				if (status == FetchListener.STATUS_OK) {
					doFetchProductType();
				} else {
					if ((BindShopActivity) getActivity() != null) {
						((BindShopActivity) getActivity())
								.showGetDateFailedToast("数据更新失败！");
					}
					return;
				}
			}
		});
	}

	/**
	 * 菜品分类信息获取
	 */
	private void doFetchProductType() {
		DataFetchManager.getInstance().fetchProductTypeInfo(
				new FetchListener() {

					@Override
					public void onPreFetch() {
					}

					@Override
					public void onPostFetch(int status, Object... result) {
						if (status == FetchListener.STATUS_OK) {
							doFetchMealPeriod();
						} else {
							if ((BindShopActivity) getActivity() != null) {
								((BindShopActivity) getActivity())
										.showGetDateFailedToast("数据更新失败！");
							}
							return;
						}
					}
				});
	}

	/**
	 * 餐段信息同步
	 */
	private void doFetchMealPeriod() {
		DataFetchManager.getInstance().fetchMealPeriod(new FetchListener() {

			@Override
			public void onPreFetch() {
			}

			@Override
			public void onPostFetch(int status, Object... result) {
				if (status == FetchListener.STATUS_OK) {
					if ((BindShopActivity) getActivity() != null) {
						((BindShopActivity) getActivity())
								.showGetDateSucceedToast("数据更新成功！");
					}
					return;
				} else {
					if ((BindShopActivity) getActivity() != null) {
						((BindShopActivity) getActivity())
								.showGetDateFailedToast("数据更新失败！");
					}
					return;
				}
			}
		});
	}

	public boolean onKeyDown(int keyCode) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// CHBApp.getInstance().exitApp();
			return getActivity().moveTaskToBack(true);
		default:
			return false;
		}
	}
	
	private void doLoginPress() {
		SysWorkTools.closeKeyboard(mWorkNumEt);
		String worknum = mWorkNumEt.getText().toString().trim();
		String pwd = mPwdEt.getText().toString().trim();
		String key = "";
		if (TextUtils.isEmpty(worknum)) {
			((BindShopActivity) getActivity()).showToast("请输入账号！");
			requireFocus(mWorkNumEt, true);
			return;
		} else if (TextUtils.isEmpty(pwd)) {
			((BindShopActivity) getActivity()).showToast("请输入密码！");
			requireFocus(mPwdEt, true);
			return;
		} else {
			doLogin(worknum, pwd);
		}
	}

	private OnEditorActionListener mWrokEtActionListener =  new TextView.OnEditorActionListener() {

		public boolean onEditorAction(TextView v, int actionId,
				KeyEvent event) {

			if (actionId == EditorInfo.IME_ACTION_NEXT
					|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
				requireFocus(mPwdEt, true);
				return true;
			}
			return false;

		}

	};
	
	private OnEditorActionListener mPwdEtActionListener =  new TextView.OnEditorActionListener() {

		public boolean onEditorAction(TextView v, int actionId,
				KeyEvent event) {

			if (actionId == EditorInfo.IME_ACTION_NONE) {
				SysWorkTools.closeKeyboard(mWorkNumEt);
				return true;
			}
			return false;

		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_agin_TV:
			if (!PrefsConfig.isCheckDevPwd) {
				confirmDevPwd();
			} else {
				CaptureFragment captureFragment = new CaptureFragment();
				((BindShopActivity) getActivity())
						.entrySubFragment(captureFragment);
			}
			break;
		case R.id.login_help_TV:
			
			break;
		default:
			break;
		}
	}
	
	

}