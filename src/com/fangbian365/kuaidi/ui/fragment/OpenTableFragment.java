package com.fangbian365.kuaidi.ui.fragment;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Diningtable;
import com.fangbian365.kuaidi.base.bean.OpenTableData;
import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.mod.asynctask.CHBHttpTask;
import com.fangbian365.kuaidi.mod.asynctask.http.FetchListener;
import com.fangbian365.kuaidi.mod.manager.DataFetchManager;
import com.fangbian365.kuaidi.ui.activity.MainActivity;
import com.fangbian365.kuaidi.ui.receiver.HomeBroadcastReceiver;
import com.fangbian365.kuaidi.ui.receiver.IDataTransfer;
import com.umeng.analytics.MobclickAgent;

/**
 * 开台选择人数
 * @author anqi
 */
public class OpenTableFragment extends HeaderFragment implements IDataTransfer {
	private TextView mTableIdTv;
	private static EditText mPersonNumEt;
	private TextView mOpenTableTv;
	private Canyin_Shop_Diningtable mOpCurTable;// 当前桌对象
	private final String TAG = OpenTableFragment.class.getSimpleName();
	
	private boolean bIsLoading = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mOpCurTable = (Canyin_Shop_Diningtable) bundle
					.getSerializable(Constans.KEY_TABLE_INFO);
		}
		HomeBroadcastReceiver.registerIDataTransfer(this);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup contentView = (ViewGroup) inflater.inflate(R.layout.fragment_opentable, null);
		mTableIdTv = (TextView) contentView.findViewById(R.id.table_id_tv);
		mTableIdTv.setText(mOpCurTable.getDtablename());
		mPersonNumEt = (EditText) contentView.findViewById(R.id.person_num_et);
		showInputMethod(getActivity()); // 自动弹出键盘
		mOpenTableTv = (TextView) contentView.findViewById(R.id.open_table_tv);
		mOpenTableTv.setOnClickListener(mOpenTableClickListener);

		return super.onCreateView(inflater, contentView, savedInstanceState);
	}

	@Override
	protected String getTitle() {
		return getString(R.string.open_table);
	}

	@Override
	protected int getMenuResId() {
		return -1;
	}

	@Override
	public boolean onKeyDown(int keyCode) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(bIsLoading) {
//				bIsLoading = false;
//				CHBHttpTask task = DataFetchManager.getInstance().mCurHttpTask;
//				((MainActivity) getActivity()).showLoading(false);
//				if(task != null) {
//					task.cancel();
//				}
				return true;
			} else {
				close();
				return true;
			}
		default:
			return false;
		}
	}

	private OnClickListener mOpenTableClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			String personNum = mPersonNumEt.getText().toString().trim();
			if (TextUtils.isEmpty(personNum)) {
				SysWorkTools.showToast(getActivity(), "请输入就餐人数");
			} else {
				try {
					if (Integer.parseInt(personNum) == 0) {
						SysWorkTools.showToast(getActivity(), "开台人数必须大于0");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					SysWorkTools.showToast(getActivity(), "开台人数必须大于0");
					return;
				}
				if (Integer.parseInt(personNum) > mOpCurTable.getMaxseat()) {
					OpenTableDialogFragment dialog = new OpenTableDialogFragment();
					Bundle bundle = new Bundle();
					bundle.putSerializable(Constans.KEY_TABLE_INFO, mOpCurTable);
					bundle.putString(Constans.KEY_TABLE_PERSON_CNT, personNum);
					dialog.setArguments(bundle);
					FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
					dialog.show(fragmentManager, "OpenTableDialogFragment");
					return;
				}
				
				OpenTableData openData = new OpenTableData(mOpCurTable);
				openData.setPersonCnt(personNum);
				openData.setMealName(SysWorkTools.showEatTime());// 空的
				openData.setSaleType(Constans.CANTEEN);

				JSONArray jsonArray = new JSONArray();
				jsonArray.add(openData);

				String jsonStr = jsonArray.toJSONString();

				FrameLog.v("tableString", jsonStr);

				openTable(jsonStr);
				
			}
		}
	};

	/**
	 * 自动显示键盘
	 * 
	 * @param context
	 */
	public static void showInputMethod(Context context) {
		mPersonNumEt.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) mPersonNumEt
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(mPersonNumEt, 0);
			}
		}, 998);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		HomeBroadcastReceiver.unregisterIDataTransfer(this);
		SysWorkTools.closeKeyboard(getActivity());
	}
	
	@Override
	public void onDataTransfer(int type, Object... data) {
		
		if (type == Constans.TYPE_OPEN_TABLE) {
			String jsonStr = (String) data[0];
			openTable(jsonStr);
			
		}
		
	}

	private void openTable(String jsonStr) {
		if (SysWorkTools.isNetAvailable()) {
			// 开台操作
			DataFetchManager.getInstance().fetchOpenTable(jsonStr, "",
					new FetchListener() {

						@Override
						public void onPreFetch() {
							bIsLoading = true;
							showLoading(true);
						}

						@Override
						public void onPostFetch(int status, Object... result) {
							bIsLoading = false;
							showLoading(false);

							if (status == FetchListener.STATUS_OK) {
								SysWorkTools.showToast(getActivity(),"开台成功！");
								close();
								TakeOrderFragment orderFragment = new TakeOrderFragment();
								Bundle bundle = new Bundle();
								bundle.putSerializable(Constans.KEY_TABLE_INFO,mOpCurTable);
								orderFragment.setArguments(bundle);
								((MainActivity) getActivity()).entrySubFragment(orderFragment);
								((MainActivity) getActivity()).doFetchTableInfo();
								
							} else {
								String msg = (String) result[0];
								SysWorkTools.showToast(getActivity(), msg);
							}
						}
					});
		}else {
			SysWorkTools.showToast(getActivity(), "请检查网络");
		}
		
	}
	
	private void showLoading(boolean bShow) {
		try {
			((MainActivity) getActivity()).showLoading(bShow);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
