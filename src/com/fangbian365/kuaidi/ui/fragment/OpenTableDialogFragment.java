package com.fangbian365.kuaidi.ui.fragment;

import com.alibaba.fastjson.JSONArray;
import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Diningtable;
import com.fangbian365.kuaidi.base.bean.OpenTableData;
import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.mod.asynctask.http.FetchListener;
import com.fangbian365.kuaidi.ui.activity.BindShopActivity;
import com.fangbian365.kuaidi.ui.activity.MainActivity;
import com.fangbian365.kuaidi.ui.receiver.HomeBroadcastReceiver;
import com.umeng.analytics.MobclickAgent;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 是否开台对话框
 */
public class OpenTableDialogFragment extends DialogFragment implements OnClickListener {
	
	private final String TAG = OpenTableDialogFragment.class.getSimpleName();
	private Canyin_Shop_Diningtable mOpCurTable;// 当前桌对象
	private String personNum;//当前人数
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mOpCurTable = (Canyin_Shop_Diningtable) bundle.getSerializable(Constans.KEY_TABLE_INFO);
			personNum = bundle.getString(Constans.KEY_TABLE_PERSON_CNT);
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.open_table_dialog, null, false);
		TextView ok_tv = (TextView) view.findViewById(R.id.tv_ok);
		ok_tv.setOnClickListener(this);
		TextView cancel_tv = (TextView) view.findViewById(R.id.tv_cancel);
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
		case R.id.tv_ok:// 继续开台
			
			dismiss();
			OpenTableData openData = new OpenTableData(mOpCurTable);
			openData.setPersonCnt(personNum);
			openData.setMealName(SysWorkTools.showEatTime());// 空的
			openData.setSaleType(Constans.CANTEEN);

			JSONArray jsonArray = new JSONArray();
			jsonArray.add(openData);

			String jsonStr = jsonArray.toJSONString();
			FrameLog.v("tableString", jsonStr);
			HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_OPEN_TABLE, jsonStr);
			dismiss();
			break;
		case R.id.tv_cancel:// 取消
			dismiss();
			break;
		default:
			break;
		}

	}
	
}
