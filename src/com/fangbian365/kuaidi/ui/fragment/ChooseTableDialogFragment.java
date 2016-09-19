package com.fangbian365.kuaidi.ui.fragment;

import java.util.List;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.CHBApp;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Diningtable;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Floor;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Product;
import com.fangbian365.kuaidi.base.bean.DCaiBean;
import com.fangbian365.kuaidi.base.bean.FenChuPrinter;
import com.fangbian365.kuaidi.base.bean.MsgProBean;
import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.mod.asynctask.http.FetchListener;
import com.fangbian365.kuaidi.mod.manager.DataFetchManager;
import com.fangbian365.kuaidi.ui.activity.MainActivity;
import com.fangbian365.kuaidi.ui.adapter.TabsAdapter;
import com.fangbian365.kuaidi.ui.receiver.HomeBroadcastReceiver;
import com.fangbian365.kuaidi.ui.receiver.IDataTransfer;
import com.fangbian365.kuaidi.ui.uisupport.MsgProductList;
import com.fangbian365.kuaidi.ui.uisupport.PickerView;
import com.fangbian365.kuaidi.ui.uisupport.ProgressView;
import com.fangbian365.kuaidi.ui.uisupport.ScllorTabView;
import com.fangbian365.kuaidi.ui.uisupport.PickerView.onSelectListener;
import com.umeng.analytics.MobclickAgent;

public class ChooseTableDialogFragment extends DialogFragment implements OnClickListener, OnCheckedChangeListener, OnPageChangeListener, IDataTransfer {
	
	private static final String TAG = "ChooseTableDialogFragment";
	private RelativeLayout mRootView;
	private PickerView mHallPicker;//picker_hall

	private RadioGroup mRgPagerTab;
	private RadioButton mRbAll;
	private RadioButton mRbIdle;
	private RadioButton mRbInuse;

	private ViewPager mTablePager;// table_page
	private ScllorTabView mTabLine;// iv_off_tab_line
	private TabsAdapter mTabsAdapter;
//	private String curHall = "全部";
	private List<Canyin_Shop_Diningtable> mTableList;
	private DbManager dbManager;
	private String mTasteStr;
	private MsgProductList<MsgProBean> productList;// 已有菜品列表
	private Canyin_Shop_Diningtable mCurTable;// 已选桌位
	private String mLslsh;//临时流水号
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTableList = ((MainActivity)getActivity()).getCurTableList();
		Bundle bundle = getArguments();
		if (bundle != null) {
			mLslsh = bundle.getString(Constans.KEY_LSLSH);
			mTasteStr = bundle.getString(Constans.KEY_TASTE);
			productList = (MsgProductList<MsgProBean>) bundle.getSerializable(Constans.KEY_ORDER_LIST);
		}
		dbManager = CHBApp.getInstance().getDbManager();
		setStyle(R.style.DialogStyle, 0);
		HomeBroadcastReceiver.registerIDataTransfer(this);
	}
	

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		ViewGroup contentView = (ViewGroup) inflater.inflate(R.layout.choose_table_dialog, null);
		mRootView = (RelativeLayout) contentView.findViewById(R.id.layout_root_view);
		contentView.findViewById(R.id.tv_sure).setOnClickListener(this);
		contentView.findViewById(R.id.tv_cancle).setOnClickListener(this);
		mHallPicker = (PickerView) contentView.findViewById(R.id.picker_hall);
		mHallPicker.setOnSelectListener(mOnHallSelectorListener);
//		mHallPicker.performSelect();
		mTablePager = (ViewPager) contentView.findViewById(R.id.table_view_pager);
		mTablePager.setOffscreenPageLimit(3);
		mTablePager.setOnPageChangeListener(this);
		
		mRgPagerTab = (RadioGroup) contentView.findViewById(R.id.rg_viewpager_tab);
		mRbAll = (RadioButton) contentView.findViewById(R.id.rb_all_table);
		mRbIdle = (RadioButton) contentView.findViewById(R.id.rb_idle_table);
		mRbInuse = (RadioButton) contentView.findViewById(R.id.rb_inuse_table);
		mRgPagerTab.setOnCheckedChangeListener(this);
		mTabLine = (ScllorTabView) contentView.findViewById(R.id.iv_off_tab_line);
		mTabLine.setTabNum(3);
		mTabLine.setCurrentNum(0);
		mTabLine.setSelectedColor(0XffFA920B);
		mTabsAdapter = new TabsAdapter(this);
		Bundle bundle = new Bundle();
		bundle.putInt(Constans.KEY_TABLE_FROM, 1);
		mTabsAdapter.addTab("All", AllTableFragment.class, bundle);
		mTabsAdapter.addTab("Idle", IdleTableFragment.class, bundle);
		mTabsAdapter.addTab("Inuse", InuseTableFragment.class, bundle);

		mTablePager.setAdapter(mTabsAdapter);
		return contentView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		List<Canyin_Shop_Floor> hList = null;
		try {
			hList = dbManager.selector(Canyin_Shop_Floor.class).findAll();//查找所有楼层信息
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(hList != null) {
			Canyin_Shop_Floor floor = new Canyin_Shop_Floor();
			floor.setFloorname("全部");
			floor.setFloorcode("全部");
			hList.add(0, floor);
			
			mHallPicker.setData(hList);
		}
		CHBApp.getInstance().getMainThreadHandler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_TABLE_CHANGED, mTableList, "全部", true);
			}
		}, 300);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
	}
	@Override
	public void onResume() {
		super.onResume();

		MobclickAgent.onPageStart(TAG);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getDialog().setCancelable(false);//CanceledOnTouchOutside(false);
	}
	
	private void mToast() {
		// TODO Auto-generated method stub
		Toast t = new Toast(getActivity());
		t.setDuration(Toast.LENGTH_SHORT);
		View view =getActivity().getLayoutInflater().inflate(R.layout.layout_toast, (ViewGroup) getActivity().findViewById(R.id.toast_layout_root));
		t.setGravity(Gravity.CENTER, 0, 0);
		t.setView(view);
		t.show();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_sure:
			if (mCurTable == null) {
				SysWorkTools.showToast(getActivity(), "请先选择台位！");
				return;
			}else {
				String tingbh =  mCurTable.getFloorcode();
				String taibh = mCurTable.getCode();
				if (mCurTable.getStatus() == 0) { //调用开台并点菜接口
					
					JSONArray jsonArray = new JSONArray();
					for (MsgProBean hpBean : productList) {
						hpBean.setTingBh(tingbh);
						hpBean.setTaiBh(taibh);
						hpBean.setLsh(mLslsh);
						hpBean.setDcyname(PrefsConfig.workerName);
						hpBean.setDcycode(PrefsConfig.workerNum);
						jsonArray.add(new DCaiBean(hpBean));
					}
					orderWMDish(mCurTable, mTasteStr, jsonArray.toJSONString());
				}else if (mCurTable.getStatus() == 1) { //调用加菜接口
					
					JSONArray jsonArray = new JSONArray();
					for (MsgProBean hpBean : productList) {
						hpBean.setTingBh(tingbh);
						hpBean.setTaiBh(taibh);
						hpBean.setLsh(mCurTable.getLsh());
						hpBean.setDcyname(PrefsConfig.workerName);
						hpBean.setDcycode(PrefsConfig.workerNum);
						jsonArray.add(new DCaiBean(hpBean));
					}
					orderDish(tingbh, taibh, mCurTable.getLsh(), mTasteStr, jsonArray.toJSONString());
				}else {
					SysWorkTools.showToast(getActivity(), "请先选择空闲或占用台位！");
				}
				
			}
			break;
		case R.id.tv_cancle:
			dismiss();
			break;	
		default:
			break;
		}
		
	}
	
	private onSelectListener mOnHallSelectorListener = new onSelectListener() {
		
		@Override
		public void onSelect(Canyin_Shop_Floor floor) {
			String curHall = floor.getFloorcode();
			HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_TABLE_CHANGED, mTableList, curHall, true);
		}
	};

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_all_table:
			mRbAll.setChecked(true);
			mTablePager.setCurrentItem(0);
			break;
		case R.id.rb_idle_table:
			mRbIdle.setChecked(true);
			mTablePager.setCurrentItem(1);
			break;
		case R.id.rb_inuse_table:
			mRbInuse.setChecked(true);
			mTablePager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int position) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {
		mTabLine.setOffset(position, positionOffset);
	}

	@Override
	public void onPageSelected(int position) {
		switch (position) {
		case 0:
			mRbAll.setChecked(true);
			break;
		case 1:
			mRbIdle.setChecked(true);
			break;
		case 2:
			mRbInuse.setChecked(true);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 单桌点菜
	 * 
	 * @param tingbh
	 * @param taibh
	 * @param ktlsh
	 * @param data
	 */
	private void orderDish(String tingbh, String taibh, final String ktlsh,String taste, String data) {
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fetchOrderDishes(tingbh, taibh, ktlsh, taste, data, new FetchListener() {

				@Override
				public void onPreFetch() {
					ProgressView.getInstance(getActivity()).showProgress(mRootView);
				}

				@SuppressWarnings("unchecked")
				@Override
				public void onPostFetch(int status, Object... result) {
					ProgressView.getInstance(getActivity()).hideProgress(mRootView);
					if (status == FetchListener.STATUS_OK) {
						SysWorkTools.showToast(getActivity(), "下单成功");
						HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_MSG_TAKE_ORDER_SUCC, mLslsh);
						dismiss();

					} else {
						SysWorkTools.showToast(getActivity(), "下单失败");
					}
				}
			});
		}else {
			SysWorkTools.showToast(getActivity(), "请检查网络");
		}
		
	}
	
	private void orderWMDish(Canyin_Shop_Diningtable table, String taste, String data) {
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fetchOrderWMDish(table.getFloorcode(),
					table.getCode(), mLslsh, taste, data, new FetchListener() {

						@Override
						public void onPreFetch() {
							ProgressView.getInstance(getActivity()).showProgress(mRootView);
						}

						@SuppressWarnings("unchecked")
						@Override
						public void onPostFetch(int status, Object... result) {
							ProgressView.getInstance(getActivity()).hideProgress(mRootView);
							if (status == FetchListener.STATUS_OK) {
								SysWorkTools.showToast(getActivity(), "下单成功");
								HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_MSG_TAKE_ORDER_SUCC, mLslsh);
								dismiss();
							}else if (status == FetchListener.StATUS_OFFLINE) {
								FrameLog.d(TAG, "status------" + status);
							}else {
								SysWorkTools.showToast(getActivity(), "下单失败");
							}
						}
					});
		}else {
			SysWorkTools.showToast(getActivity(), "请检查网络");
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		HomeBroadcastReceiver.unregisterIDataTransfer(this);
	}
	
	@Override
	public void onDataTransfer(int type, Object... data) {
		if (type == Constans.TYPE_CHOOSE_TABLE) {
			mCurTable = (Canyin_Shop_Diningtable) data[0];
		}
		
	}

}