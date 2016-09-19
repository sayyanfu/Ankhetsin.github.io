package com.fangbian365.kuaidi.ui.fragment;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.ui.activity.BindShopActivity;
import com.umeng.analytics.MobclickAgent;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 退出账户对话框
 */
public class ExitAccountFragment extends DialogFragment implements OnClickListener {
	
	private final String TAG = ExitAccountFragment.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.exit_account_dialog, null, false);
		TextView ok_tv = (TextView) view.findViewById(R.id.tv_exit_app_ok);
		ok_tv.setOnClickListener(this);
		TextView cancel_tv = (TextView) view.findViewById(R.id.tv_exit_app_calcel);
		cancel_tv.setOnClickListener(this);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);

		return dialog;
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
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.tv_exit_app_ok:// 确认退出
			PrefsConfig.clearUser();
			Intent intent = new Intent(getActivity(), BindShopActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString(Constans.LOGOUT, "logout");
			intent.putExtras(bundle);
			startActivity(intent);
			getActivity().finish();
			break;
		case R.id.tv_exit_app_calcel:// 取消
			ExitAccountFragment.this.dismiss();
			break;
		default:
			break;
		}

	}
	
}
