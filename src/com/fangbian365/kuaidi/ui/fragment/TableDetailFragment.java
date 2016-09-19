package com.fangbian365.kuaidi.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Diningtable;
import com.fangbian365.kuaidi.base.bean.Elice;
import com.fangbian365.kuaidi.base.bean.HaveProductBean;
import com.fangbian365.kuaidi.base.bean.RemindDish;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.mod.asynctask.http.FetchListener;
import com.fangbian365.kuaidi.mod.manager.DataFetchManager;
import com.fangbian365.kuaidi.ui.activity.MainActivity;
import com.fangbian365.kuaidi.ui.adapter.HaveProductAdapter;
import com.fangbian365.kuaidi.ui.receiver.HomeBroadcastReceiver;
import com.fangbian365.kuaidi.ui.receiver.IDataTransfer;
import com.umeng.analytics.MobclickAgent;
import com.zhiren.swipelistview.SwipeMenu;
import com.zhiren.swipelistview.SwipeMenuCreator;
import com.zhiren.swipelistview.SwipeMenuItem;
import com.zhiren.swipelistview.SwipeMenuListView;
import com.zhiren.swipelistview.SwipeMenuListView.OnMenuItemClickListener;


public class TableDetailFragment extends HeaderFragment implements OnItemClickListener, IDataTransfer {
	private static final String TAG = "TableDetailFragment";
	
	private TextView mTvName;//tv_table_name
	private TextView mTvWaiter;//tv_waiter_name
	private TextView mTvTime;//tv_table_time
	private TextView mTvLsh;//tv_lsh
	
	private SwipeMenuListView mProList;//product_list
	private TextView mTvPinXiangNum;//tv_pinxiang_num
	private TextView mTvTotalPrice;//tv_total_price
	private TextView mTvProNum;//tv_pro_num
	
	private boolean bCanOpen = true;
	private TextView mTvRemarks;//tv_remarks
	private ImageView mIvRemarkTag;//iv_remark_tag
	
	private boolean bIsLoading = false;
	
	private Canyin_Shop_Diningtable mTable;
	private List<HaveProductBean> mHaveDishList;// 已点菜品
	private HaveProductAdapter haveProdAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle != null) {
			mTable = (Canyin_Shop_Diningtable) bundle.getSerializable(Constans.KEY_TABLE_INFO);
		}
		mHaveDishList = new ArrayList<HaveProductBean>();
		HomeBroadcastReceiver.registerIDataTransfer(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		HomeBroadcastReceiver.unregisterIDataTransfer(this);
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
		ViewGroup contentView = (ViewGroup) inflater.inflate(R.layout.table_detail_fragment, null);
		mTvName = (TextView) contentView.findViewById(R.id.tv_table_name);
		mTvWaiter = (TextView) contentView.findViewById(R.id.tv_waiter_name);
		mTvTime = (TextView) contentView.findViewById(R.id.tv_table_time);
		mTvLsh = (TextView) contentView.findViewById(R.id.tv_lsh);
		
		mTvName.setText(mTable.getDtablename());
		mTvWaiter.setText(PrefsConfig.workerName);
		String openTableTime = Constans.DATE_FORMAT.format(mTable.getStarttime());
		mTvTime.setText(openTableTime);
		mTvLsh.setText(mTable.getLsh());
		mProList = (SwipeMenuListView) contentView.findViewById(R.id.product_list);
		haveProdAdapter = new HaveProductAdapter(getActivity());
		mProList.setAdapter(haveProdAdapter);
		mProList.setMenuCreator(creator);
		mProList.setOnMenuItemClickListener(mOnMenuItemClickListaner);
		
		mProList.setOnItemClickListener(this);
		
		mTvPinXiangNum = (TextView) contentView.findViewById(R.id.tv_pinxiang_num);
		mTvTotalPrice = (TextView) contentView.findViewById(R.id.tv_total_price);
		mTvProNum = (TextView) contentView.findViewById(R.id.tv_pro_num);
		
		mTvRemarks = (TextView) contentView.findViewById(R.id.tv_remarks);
		mIvRemarkTag = (ImageView) contentView.findViewById(R.id.iv_remark_tag);
		mIvRemarkTag.setOnClickListener(this);
		
		contentView.findViewById(R.id.iv_gaidan).setOnClickListener(this);
		contentView.findViewById(R.id.iv_add_product).setOnClickListener(this);
		contentView.findViewById(R.id.iv_cuidan).setOnClickListener(this);
		contentView.findViewById(R.id.iv_print).setOnClickListener(this);
		
		return super.onCreateView(inflater, contentView, savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		doFetchProduceAll();
	}
	
	String remarks;
	private void doFetchProduceAll() {
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fetchDinnerList(mTable.getFloorcode(), mTable.getCode(), mTable.getLsh(), new FetchListener() {
				
				@Override
				public void onPreFetch() {
					showLoading(true);
				}
				
				@SuppressWarnings("unchecked")
				@Override
				public void onPostFetch(int status, Object... result) {
					showLoading(false);
					
					if (status == FetchListener.STATUS_OK) {
						mHaveDishList = (List<HaveProductBean>) result[0];
						remarks = (String) result[1];
						String waiterCode = (String) result[3];
						String waiterName = (String) result[4];
						if (!TextUtils.isEmpty(waiterName) && !waiterName.equals(PrefsConfig.workerName)) {
							mTvWaiter.setText(waiterName);
						}
						refrashHaveProductUI(remarks);
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
	
	private void refrashHaveProductUI(String remarks) {
		if (mHaveDishList == null) {
			return;
		}
		haveProdAdapter.setItems(mHaveDishList);
		haveProdAdapter.notifyDataSetChanged();
		
		float priceTotal = 0.0f;
		float proNum = 0.0f;
		for (HaveProductBean item : mHaveDishList) {
			if (item.getStatus() != 3) {// 已下单 不是退菜
				priceTotal += item.getPrice() * item.getCnt();
				proNum += item.getCnt();
			}
		}
		mTvTotalPrice.setText("￥" + Constans.DF.format(priceTotal));
		List<String> mList = new ArrayList<String>();
		for (int i = 0; i < mHaveDishList.size(); i++) {//获取到要比较的字段
			if(mHaveDishList.get(i).getStatus() != 3) {
				mList.add(mHaveDishList.get(i).getCode());
			}
		}
		for (int i = 0; i < mList.size(); i++) {// 比较是否有相同数据

			int j = mList.lastIndexOf(mList.get(i));

			if (i != j ) {
				mList.remove(j);
				i--;// 每次有重复都让i回到初始位置，i==j时才可以向下循环
			}
		}
		mTvPinXiangNum.setText(mList.size() + "项");
		mTvProNum.setText(Constans.DF.format(proNum));
		mTvRemarks.setText(remarks);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Fragment f;
		switch (v.getId()) {
		case R.id.iv_remark_tag:
			if(bCanOpen) {
				mTvRemarks.setMaxLines(10);  
				mTvRemarks.requestLayout();
				mIvRemarkTag.setImageResource(R.drawable.up_marks);
				bCanOpen = false;
			} else {
				mTvRemarks.setMaxLines(1);  
				mTvRemarks.requestLayout();
				mIvRemarkTag.setImageResource(R.drawable.down_marks);
				bCanOpen = true;
			}
			
			break;
		case R.id.iv_add_product:
			f = new TakeOrderFragment();
			Bundle bundle = new Bundle();
			bundle.putString("taste", remarks);
			bundle.putSerializable(Constans.KEY_TABLE_INFO, mTable);
			f.setArguments(bundle);
			
			((MainActivity) getActivity()).entrySubFragment(f);
			break;
		case R.id.iv_gaidan:
			
			DialogFragment dialog = new ProductOperationDialogFragment();
			bundle = new Bundle();
			bundle.putInt(Constans.KEY_PRO_DIALOG_TYPE, 4);
			bundle.putSerializable(Constans.KEY_TABLE_INFO, mTable);
			dialog.setArguments(bundle);
			dialog.show(getChildFragmentManager(), "ProductOperationDialogFragment");
			
			break;

		case R.id.iv_cuidan: //整单催菜
			remindAllDish();
			break;
		case R.id.iv_print:
			if (SysWorkTools.isNetAvailable()) {
				DataFetchManager.getInstance().fetchprintYJD(mTable.getLsh(), new FetchListener() {
					
					@Override
					public void onPreFetch() {
						bIsLoading = true;
						showLoading(true);
					}
					
					@Override
					public void onPostFetch(int status, Object... result) {
						if (status == FetchListener.STATUS_OK) {
							
							SysWorkTools.showToast(getActivity(), "打印成功");

						} else {
							SysWorkTools.showToast(getActivity(), "打印失败");
						}
						
						showLoading(false);
						bIsLoading = false;
					}
				});
			}else {
				SysWorkTools.showToast(getActivity(), "请检查网络");
			}
			
			break;

		default:
			break;
		}
		
	}

	private SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
        	SwipeMenuItem menuItem;
        	menuItem = new SwipeMenuItem(getActivity());
            menuItem.setBackground(new ColorDrawable(Color.rgb(0xFD, 0x8F,  0x09)));
        	menuItem.setWidth(SysWorkTools.dp2px(getActivity(), 60));
        	menuItem.setIcon(R.drawable.iv_swipe_list_zhuancai);
            menu.addMenuItem(menuItem);
            
            menuItem = new SwipeMenuItem(getActivity());
            menuItem.setBackground(new ColorDrawable(Color.rgb(0xFD, 0x8F,  0x09)));
        	menuItem.setWidth(SysWorkTools.dp2px(getActivity(), 60));
        	menuItem.setIcon(R.drawable.iv_swipe_list_tuicai);
            menu.addMenuItem(menuItem);
            
            menuItem = new SwipeMenuItem(getActivity());
            menuItem.setBackground(new ColorDrawable(Color.rgb(0xFD, 0x8F,  0x09)));
        	menuItem.setWidth(SysWorkTools.dp2px(getActivity(), 60));
        	menuItem.setIcon(R.drawable.iv_swipe_list_zengcai);
            menu.addMenuItem(menuItem);
        	
            menuItem = new SwipeMenuItem(getActivity());
            menuItem.setBackground(new ColorDrawable(Color.rgb(0xFD, 0x8F,  0x09)));
        	menuItem.setWidth(SysWorkTools.dp2px(getActivity(), 60));
        	menuItem.setIcon(R.drawable.iv_swipe_list_huacai);
            menu.addMenuItem(menuItem);
            
        	menuItem = new SwipeMenuItem(getActivity());
        	menuItem.setBackground(new ColorDrawable(Color.rgb(0xFD, 0x8F,  0x09)));
        	menuItem.setWidth(SysWorkTools.dp2px(getActivity(), 60));
        	menuItem.setIcon(R.drawable.iv_swipe_list_cuicai);
            menu.addMenuItem(menuItem);

        }
    };
    
	private OnMenuItemClickListener mOnMenuItemClickListaner = new OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
			HaveProductBean item = mHaveDishList.get(position);
			
			DialogFragment dialog;
			Bundle bundle;
			switch (index) {
			case 0:// 转菜
				
				if (item.getStatus() == 3) {
					SysWorkTools.showToast(getActivity(), "菜品已退");
					return false;
				}
				dialog = new ProductOperationDialogFragment();
				bundle = new Bundle();
				bundle.putInt(Constans.KEY_PRO_DIALOG_TYPE, 1);
				bundle.putSerializable(Constans.KEY_PRO_DETAIL, item);
				bundle.putSerializable(Constans.KEY_TABLE_INFO, mTable);
				dialog.setArguments(bundle);
				dialog.show(getChildFragmentManager(), "ProductOperationDialogFragment");
				
				break;
			case 1:// 退菜
				if (item.getStatus() == 3) {
					SysWorkTools.showToast(getActivity(), "菜品已退");
					return false;
				}
				if (item.getStatus() == 2) {
					SysWorkTools.showToast(getActivity(), "菜品已赠");
					return false;
				}
				dialog = new ProductOperationDialogFragment();
				bundle = new Bundle();
				bundle.putInt(Constans.KEY_PRO_DIALOG_TYPE, 2);
				bundle.putSerializable(Constans.KEY_PRO_DETAIL, item);
				dialog.setArguments(bundle);
				dialog.show(getChildFragmentManager(), "ProductOperationDialogFragment");
				
				break;
			case 2:// 赠菜
				if (item.getStatus() == 3) {
					SysWorkTools.showToast(getActivity(), "菜品已退");
					return false;
				}
				if (item.getStatus() == 2) {
					SysWorkTools.showToast(getActivity(), "菜品已赠");
					return false;
				}
				dialog = new ProductOperationDialogFragment();
				bundle = new Bundle();
				bundle.putInt(Constans.KEY_PRO_DIALOG_TYPE, 3);
				bundle.putSerializable(Constans.KEY_PRO_DETAIL, item);
				dialog.setArguments(bundle);
				dialog.show(getChildFragmentManager(), "ProductOperationDialogFragment");
				
				break;
			case 3:// 划菜
				if (item.getMarkStatus() != null && (item.getMarkStatus().equals("3") || item.getMarkStatus().equals("已上"))){
					SysWorkTools.showToast(getActivity(), "菜品已上");
					return false;
				}
				if (item.getStatus() == 3) {
					SysWorkTools.showToast(getActivity(), "菜品已退");
					return false;
				}
				doFecherEliceDish(item);
				break;
			case 4:// 催菜
				if (item.getMarkStatus() != null && (item.getMarkStatus().equals("3") || item.getMarkStatus().equals("已上"))){
					SysWorkTools.showToast(getActivity(), "菜品已上");
					return false;
				}
				if (item.getStatus() == 3) {
					SysWorkTools.showToast(getActivity(), "菜品已退");
					return false;
				}
				remindDish(position);
				break;
			}
			return false;
		}

		
	};
	
	/**
	 * 划菜请求
	 */
	private void doFecherEliceDish(final HaveProductBean bean) {
		Elice elice = new Elice();
		elice.setId(bean.getpId());
		elice.setMarkstatus("3");
		JSONArray jaArray = new JSONArray();
		jaArray.add(elice);
		String data = jaArray.toJSONString();
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fecthEliceDishes(data,
					new FetchListener() {

						@Override
						public void onPreFetch() {
							bIsLoading = true;
							showLoading(true);
						}

						@Override
						public void onPostFetch(int status, Object... result) {
							
							if (status == FetchListener.STATUS_OK) {
								SysWorkTools.showToast(getActivity(), "划菜成功");
								bean.setMarkStatus("3");//设置当前数据划菜标志为3
								haveProdAdapter.notifyDataSetChanged();//刷新界面

							} else if (status == FetchListener.StATUS_OFFLINE) {
								SysWorkTools.showToast(getActivity(), "划菜失败");
							}
							showLoading(false);
							bIsLoading = false;
						}
					});
		}else {
			SysWorkTools.showToast(getActivity(), "请检查网络");
		}
		
	}
	
	/**
	 * 单品催菜
	 * @param position
	 */
	private void remindDish(int position) {

		final HaveProductBean item = mHaveDishList.get(position);
		String ktlsh = mTable.getLsh();// 获取流水号
		String tingbh = mTable.getFloorcode();// 楼层号
		String taibh = mTable.getCode();// 台编号
		String ccType = "1";
		RemindDish remindDish = new RemindDish();// 起叫状态bean
		remindDish.setId(item.getpId());
		remindDish.setWaitstatus("催菜");
		JSONArray jaArray = new JSONArray();
		jaArray.add(remindDish);
		String data = jaArray.toJSONString();
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fetchRemindDish(ktlsh, tingbh, taibh,
					ccType, data, new FetchListener() {

						@Override
						public void onPreFetch() {
							bIsLoading = true;
							showLoading(true);
						}

						@Override
						public void onPostFetch(int status, Object... result) {
							if (status == FetchListener.STATUS_OK) {
								
								SysWorkTools.showToast(getActivity(), item.getCodeName() + "已催");
								if (mHaveDishList == null) {
									return;
								}
								item.setWaitStatus("催菜");
								haveProdAdapter.setItems(mHaveDishList);
								haveProdAdapter.notifyDataSetChanged();
							} else {
								SysWorkTools.showToast(getActivity(), "催菜失败，请重试");
							}
							
							showLoading(false);
							bIsLoading = false;
						}
					});
		}else {
			SysWorkTools.showToast(getActivity(), "请检查网络");
		}
		
	}
	
	/**
	 * 整单催菜 
	 */
	private void remindAllDish() {
		String ktlsh = mTable.getLsh();// 获取流水号
		String tingbh = mTable.getFloorcode();// 楼层号
		String taibh = mTable.getCode();// 台编号
		String ccType = "2";
		String data = "";
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fetchRemindDish(ktlsh, tingbh, taibh,
					ccType, data, new FetchListener() {

						@Override
						public void onPreFetch() {
							bIsLoading = true;
							showLoading(true);
						}

						@Override
						public void onPostFetch(int status, Object... result) {
							showLoading(false);
							if (status == FetchListener.STATUS_OK) {
								SysWorkTools.showToast(getActivity(), "已催");
								for (HaveProductBean bean : mHaveDishList) {
									bean.setWaitStatus("催菜");
								}
								
								haveProdAdapter.setItems(mHaveDishList);
								haveProdAdapter.notifyDataSetChanged();
								
							} else {
								SysWorkTools.showToast(getActivity(), "催菜失败，请重试");
							}
							
							bIsLoading = false;
						}
					});
		}else {
			SysWorkTools.showToast(getActivity(), "请检查网络");

		}
		
	}
	

	@Override
	protected String getTitle() {
		return "点单(已下单)";
	}

	@Override
	protected int getMenuResId() {
		return 0;
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}

	@Override
	public void onDataTransfer(int type, Object... data) {
		switch (type) {
		case Constans.TYPE_ZHUANCAI_SUCC:
		case Constans.TYPE_TUICAI_SUCC:
		case Constans.TYPE_ZENGCAI_SUCC:
		case Constans.TYPE_SUCCESS_ORDER_DISH:	
		case Constans.TYPE_GAIDAN_SUCC:
			doFetchProduceAll();
			break;
		case Constans.TYPE_CHANGE_WORKNAME:
			break;
		default:
			break;
		}
	}
	
	private void changeWorkName(String workName, String workerNum) {
		mTvWaiter.setText(workName);
	}

}