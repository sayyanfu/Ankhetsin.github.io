package com.fangbian365.kuaidi.ui.fragment;

import java.util.List;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.CHBApp;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Taste;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.ui.receiver.HomeBroadcastReceiver;
import com.fangbian365.kuaidi.ui.uisupport.AutoBreakLayout;
import com.umeng.analytics.MobclickAgent;

/**
 * 单品备注界面
 */
public class SingleDishRemarkFragment extends HeaderFragment implements
		OnCheckedChangeListener, OnClickListener {
	private EditText mCumtomsEt;
	private TextView mSureTv;// 确认添加
	private AutoBreakLayout mRemarkLayout;
	private DbManager dbManager;
	private List<Canyin_Shop_Taste> tasteList; // 备注数据
	private String tasteStr = "";
	private RadioButton mWaitCallCb;
	private RadioButton mThatIsCb;
	private int mPosition = 0;
	private String waitStatus = "";

	private final String TAG = SingleDishRemarkFragment.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mPosition = bundle.getInt(Constans.TAKE_ORDER_POSITION);
			waitStatus = bundle.getString(Constans.WAIT_STATUS_TASTE);
		}

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
				R.layout.fragment_sigledishremarks, null);
		mRemarkLayout = (AutoBreakLayout) contentView
				.findViewById(R.id.remarks_auto_break);
		mCumtomsEt = (EditText) contentView.findViewById(R.id.remark_input_et);
		mCumtomsEt.addTextChangedListener(watcher);
		mSureTv = (TextView) contentView.findViewById(R.id.sure_tv);
		mSureTv.setOnClickListener(mSureBtnListener);
		mWaitCallCb = (RadioButton) contentView.findViewById(R.id.tv_wait_call);
		mThatIsCb = (RadioButton) contentView.findViewById(R.id.tv_that_is);
		if (waitStatus.equals("即起")) {
			mWaitCallCb.setChecked(false);
			mThatIsCb.setChecked(true);
		} else if (waitStatus.equals("等叫")) {
			mWaitCallCb.setChecked(true);
			mThatIsCb.setChecked(false);
		}
		mWaitCallCb.setOnClickListener(this);
		mThatIsCb.setOnClickListener(this);
		dbManager = CHBApp.getInstance().getDbManager();
		try {
			tasteList = dbManager.findAll(Canyin_Shop_Taste.class);
		} catch (DbException e) {
			e.printStackTrace();
		}

		updateRemarkView();

		return super.onCreateView(inflater, contentView, savedInstanceState);
	}

	@Override
	protected String getTitle() {
		return getString(R.string.single_remarks);
	}

	@Override
	protected int getMenuResId() {
		return 0;
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

	/**
	 * 确认添加
	 */
	private OnClickListener mSureBtnListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			StringBuilder sb = new StringBuilder();
			for (Canyin_Shop_Taste taste : tasteList) {
				if (taste.isChoosed()) {
					sb.append(taste.getTastename());
					sb.append(",");
				}
			}
			String customs = mCumtomsEt.getText().toString().trim();
			if (!TextUtils.isEmpty(customs)) {
				sb.append(customs);
				if (sb.length() > 0) {
					tasteStr = sb.substring(0, sb.length());
				}
			} else {
				if (sb.length() > 0) {
					tasteStr = sb.substring(0, sb.length() - 1);
				}
			}

			HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_SINGLE_TASTE,
					mPosition, tasteStr, waitStatus);

			close();

		}
	};

	/**
	 * 搜索功能监听
	 */
	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable arg0) {

		}
	};

	private void updateRemarkView() {
		mRemarkLayout.removeAllViews();
		if (tasteList == null) {
			return;
		}
		
		View view = null;
		CheckBox hotWordView = null;

		if (tasteList != null && tasteList.size() > 0) {
			for (Canyin_Shop_Taste item : tasteList) {
				if (TextUtils.isEmpty(item.getTastename())) {
					continue;
				}
				view = View.inflate(getActivity(),
						R.layout.library_hotwords_item2, null);

				hotWordView = (CheckBox) view;
				hotWordView.setText(item.getTastename());
				hotWordView.setOnCheckedChangeListener(this);
				FrameLayout.LayoutParams lp = new LayoutParams(
						FrameLayout.LayoutParams.WRAP_CONTENT,
						FrameLayout.LayoutParams.WRAP_CONTENT); // 必须设置参数.否则是fill_parent
				mRemarkLayout.addView(hotWordView, lp);
			}
			mRemarkLayout.setVisibility(View.VISIBLE);
		} else {
			mRemarkLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String taste = buttonView.getText().toString();
		for (Canyin_Shop_Taste item : tasteList) {
			if (item.getTastename().equals(taste)) {
				if (item.isChoosed()) {
					item.setChoosed(false);
				} else {
					item.setChoosed(true);
				}
				break;
			}
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.tv_wait_call:
			mWaitCallCb.setChecked(true);
			mThatIsCb.setChecked(false);
			waitStatus = mWaitCallCb.getText().toString();
			break;
		case R.id.tv_that_is:
			mWaitCallCb.setChecked(false);
			mThatIsCb.setChecked(true);
			waitStatus = mThatIsCb.getText().toString();
			break;
		default:
			break;
		}

	}

}
