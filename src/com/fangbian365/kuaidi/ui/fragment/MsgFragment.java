package com.fangbian365.kuaidi.ui.fragment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.MsgBean;
import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.mod.asynctask.http.FetchListener;
import com.fangbian365.kuaidi.mod.manager.DataFetchManager;
import com.fangbian365.kuaidi.ui.activity.MainActivity;
import com.fangbian365.kuaidi.ui.adapter.MsgAdapter;
import com.fangbian365.kuaidi.ui.receiver.HomeBroadcastReceiver;
import com.fangbian365.kuaidi.ui.receiver.IDataTransfer;
import com.fangbian365.kuaidi.ui.receiver.IReceiver;
import com.umeng.analytics.MobclickAgent;
import com.zhiren.swipelistview.SwipeMenu;
import com.zhiren.swipelistview.SwipeMenuCreator;
import com.zhiren.swipelistview.SwipeMenuItem;
import com.zhiren.swipelistview.SwipeMenuListView;
import com.zhiren.swipelistview.SwipeMenuListView.OnMenuItemClickListener;


public class MsgFragment extends HeaderFragment implements IReceiver, IDataTransfer {
	
	private final String TAG =MsgFragment.class.getSimpleName();
	
	private SwipeMenuListView mLvMsg;
	private MsgAdapter mMsgAdapter;
	private List<MsgBean> msgList;

	private ImageView mIvNoMsg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HomeBroadcastReceiver.registerFragmentReceiver(this);
		HomeBroadcastReceiver.registerIDataTransfer(this);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		HomeBroadcastReceiver.unregisterFragmentReceiver(this);
		HomeBroadcastReceiver.registerIDataTransfer(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup contentView = (ViewGroup) inflater.inflate(R.layout.fragment_msg, null);
		mLvMsg = (SwipeMenuListView) contentView.findViewById(R.id.lv_msg);
		mMsgAdapter = new MsgAdapter(getActivity());
		mLvMsg.setAdapter(mMsgAdapter);
		mLvMsg.setMenuCreator(creator);
		mLvMsg.setOnMenuItemClickListener(mOnMenuItemClickListener);
		mLvMsg.setOnItemClickListener(mOnItemClickListener);
		mIvNoMsg = (ImageView)contentView.findViewById(R.id.iv_no_msg);
		
		return super.onCreateView(inflater, contentView, savedInstanceState);
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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (SysWorkTools.isNetAvailable()) {
			doFetchMsg();
		}else {
			SysWorkTools.showToast(getActivity(), "请检查网络");
		}
	}
	
	private void doFetchMsg() {
		
		DataFetchManager.getInstance().fetchMsg(new FetchListener() {
			
			@Override
			public void onPreFetch() {
				showLoading(true);
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public void onPostFetch(int status, Object... result) {
				showLoading(false);
				if(status == FetchListener.STATUS_OK) {
					mLvMsg.setVisibility(View.VISIBLE);
					msgList = (List<MsgBean>) result[0];
					mMsgAdapter.setItems(msgList);
					mMsgAdapter.notifyDataSetChanged();
					mIvNoMsg.setVisibility(View.GONE);
					
				} else {
					mLvMsg.setVisibility(View.GONE);
					String msg = (String) result[0];
					SysWorkTools.showToast(getActivity(), msg);
					mIvNoMsg.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	protected String getTitle() {
		return "消息";
	}

	@Override
	protected int getMenuResId() {
		return 0;
	}

	@Override
	public boolean onKeyDown(int keyCode) {
		return false;
	}
	
	private SwipeMenuCreator creator = new SwipeMenuCreator() {

		@Override
		public void create(SwipeMenu menu) {
			SwipeMenuItem menuItem;
			menuItem = new SwipeMenuItem(getActivity());
			menuItem.setBackground(new ColorDrawable(Color
					.rgb(0xFE, 0x1A, 0x1A)));
			menuItem.setWidth(SysWorkTools.dp2px(getActivity(), 90));
			menuItem.setIcon(R.drawable.iv_swipe_list_delete);
			menu.addMenuItem(menuItem);

		}
	};
	
	private OnMenuItemClickListener mOnMenuItemClickListener = new OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
			Fragment f;
			switch (index) {
			case 0:
				if (SysWorkTools.isNetAvailable()) {
					doFetchLsProductDel(position);
				}else {
					SysWorkTools.showToast(getActivity(), "请检查网络");
				}
				
				break;
			}
			return false;
		}
	};
	
	private void doFetchLsProductDel(final int pos) {
		String lslsh = msgList.get(pos).getLslsh();
		DataFetchManager.getInstance().fetchLsProductDel(lslsh, new FetchListener() {
			
			@Override
			public void onPreFetch() {
				showLoading(true);
			}
			
			@Override
			public void onPostFetch(int status, Object... result) {
				showLoading(false);
				if (status == FetchListener.STATUS_OK) {
					msgList.remove(pos);// 删除当前位置数据
					mMsgAdapter.notifyDataSetChanged();
					
				} else {
					String msg = (String) result[0];
					SysWorkTools.showToast(getActivity(), msg);
				}
			}
		});
	}
	
	private void showLoading(boolean bShow) {
		try {
			((MainActivity) getActivity()).showLoading(bShow);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			MsgBean msg = (MsgBean) msgList.get(position);
			
			Fragment f = new MsgTakeOrderFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable(Constans.KEY_MSG_ITEM, msg);
			f.setArguments(bundle);
			((MainActivity)getActivity()).entrySubFragment(f);
			
		}
	};

	@Override
	public void onReceive(Context context, Intent intent) {
		 String action = intent.getAction();
		 if(action.equals(Constans.ACTION_RECEIVER_PUSH_MSG)){
			 doFetchMsg();
		} 
		
	}
	@Override
	public void onDataTransfer(int type, Object... data) {
		if (type == Constans.TYPE_MSG_DELETE) {
			String lslsh = (String) data[0];
			for (int i = 0; i < msgList.size(); i++) {
				if (lslsh.equals(msgList.get(i).getLslsh())) {
					msgList.remove(i);
					break;
				}
			}
			mMsgAdapter.notifyDataSetChanged();
			MsgFragment.this.close();
		}
		
	}
	
	
}
