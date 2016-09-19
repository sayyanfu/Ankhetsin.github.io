package com.fangbian365.kuaidi.ui.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.CHBApp;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Diningtable;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Product;
import com.fangbian365.kuaidi.base.bean.DCaiBean;
import com.fangbian365.kuaidi.base.bean.FenChuPrinter;
import com.fangbian365.kuaidi.base.bean.HaveProductBean;
import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.mod.asynctask.CHBHttpTask;
import com.fangbian365.kuaidi.mod.asynctask.http.FetchListener;
import com.fangbian365.kuaidi.mod.manager.DataFetchManager;
import com.fangbian365.kuaidi.ui.activity.MainActivity;
import com.fangbian365.kuaidi.ui.adapter.PinyinAdapter;
import com.fangbian365.kuaidi.ui.adapter.ProductAdapter;
import com.fangbian365.kuaidi.ui.fragment.HeaderFragment.OnRightMenuClickListener;
import com.fangbian365.kuaidi.ui.receiver.HomeBroadcastReceiver;
import com.fangbian365.kuaidi.ui.receiver.IDataTransfer;
import com.fangbian365.kuaidi.ui.uisupport.ProductList;
import com.umeng.analytics.MobclickAgent;
import com.zhiren.swipelistview.SwipeMenu;
import com.zhiren.swipelistview.SwipeMenuCreator;
import com.zhiren.swipelistview.SwipeMenuItem;
import com.zhiren.swipelistview.SwipeMenuListView;
import com.zhiren.swipelistview.SwipeMenuListView.OnMenuItemClickListener;

/**
 * 点菜
 */
public class TakeOrderFragment extends HeaderFragment implements
		OnRightMenuClickListener, OnItemSelectedListener, IDataTransfer {

	private static final String TAG = "TakeOrderFragment";
	private boolean bCanOpen = true;
	private EditText mSearchEt;
	private ImageView mSearchIv;
	private SwipeMenuListView mDishLv; // 已点菜列表
	private ListView mPinyinLv; // 拼音列表
	//private TextView mWholeRemarksTv;
	private ImageView iv_addRemark;//添加整单备注
	private TextView mSureTakeOrderTv;
	private RelativeLayout mMiddleRl;
	private TextView mProductPhaseTotalTv; // 品项合计
	private TextView mPriceTotalTv; // 价格合计
	private TextView mNumTotalTv; // 数量合计
	private TextView mMarksTv;
	private TextView mCancelSearchTv;
	private ImageView mShowMarksIv;
	private Canyin_Shop_Diningtable mOpCurTable;// 当前桌对象
	private DbManager dbManager;
	private PinyinAdapter pinyinAdapter; // 拼音适配器
	private List<Canyin_Shop_Product> pinyinList;// 拼音菜品数据
	private ProductAdapter productAdapter; // 添加菜品后的adapter
	private ProductList<HaveProductBean> productList;// 已有菜品列表
	private String tasteStr = ""; // 整单备注数据
	private RelativeLayout mSearchRl;
	private Canyin_Shop_Product selectProduct;
	private double noPayPrice = 0.0;
	private LinearLayout mTongjiLinlout; // 价格合计显示
	private RelativeLayout mRemarkRelalout; // 整单备注UI
	
	private boolean bIsLoading = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			if (bundle.getSerializable(Constans.KEY_TABLE_INFO) != null) {
				mOpCurTable = (Canyin_Shop_Diningtable) bundle
						.getSerializable(Constans.KEY_TABLE_INFO);
			}
			tasteStr = bundle.getString("taste");
		}
		dbManager = CHBApp.getInstance().getDbManager();
		productList = new ProductList<HaveProductBean>();
		
		FrameLog.e(TAG, tasteStr + "*********************88");
		HomeBroadcastReceiver.registerIDataTransfer(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup contentView = (ViewGroup) inflater.inflate(R.layout.fragment_order_dish, null);
		mSearchEt = (EditText) contentView.findViewById(R.id.et_search);
		mSearchEt.addTextChangedListener(watcher);
		mSearchIv = (ImageView) contentView.findViewById(R.id.iv_search_tag);
		mDishLv = (SwipeMenuListView) contentView
				.findViewById(R.id.dish_listview); // 菜品列表 mDishLv
		productAdapter = new ProductAdapter(getActivity());
		mDishLv.setAdapter(productAdapter);
		mDishLv.setMenuCreator(creator);
		mDishLv.setOnMenuItemClickListener(mOnMenuItemClickListener);
		iv_addRemark = (ImageView) contentView
				.findViewById(R.id.iv_remark_add);
		mSureTakeOrderTv =   (TextView) contentView
				.findViewById(R.id.tv_sure_remarks);
		mMiddleRl = (RelativeLayout) contentView
				.findViewById(R.id.relativeLayout_middle);
		mTongjiLinlout = (LinearLayout) contentView
				.findViewById(R.id.tongji_layout);
		mRemarkRelalout = (RelativeLayout) contentView
				.findViewById(R.id.rl_remark);
		mProductPhaseTotalTv = (TextView) contentView
				.findViewById(R.id.tv_pinxiang_num);
		mPriceTotalTv = (TextView) contentView
				.findViewById(R.id.tv_total_price);
		mCancelSearchTv = (TextView) contentView
				.findViewById(R.id.tv_search_cancle);
		mCancelSearchTv.setOnClickListener(cancelSearchListener);
		mSearchRl = (RelativeLayout) contentView
				.findViewById(R.id.ralative_search);
		mPinyinLv = (ListView) contentView.findViewById(R.id.search_listview);
		pinyinAdapter = new PinyinAdapter(getActivity());
		mPinyinLv.setAdapter(pinyinAdapter);
		mPinyinLv.setOnItemClickListener(pinyinItemClickListener);
		mNumTotalTv = (TextView) contentView.findViewById(R.id.tv_pro_num);
		mMarksTv = (TextView) contentView.findViewById(R.id.tv_remarks);

		mShowMarksIv = (ImageView) contentView.findViewById(R.id.iv_remark_tag);
		mDishLv.setOnItemSelectedListener(this);
		mSearchIv.setOnClickListener(mSearchBtnListener);
		mShowMarksIv.setOnClickListener(mShowMarkListener);
		iv_addRemark.setOnClickListener(mWholeRemarkListener);
		mSureTakeOrderTv.setOnClickListener(mSureTakeOrderLstener);
		setOnRightMenuClickListener(this);

		return super.onCreateView(inflater, contentView, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		List<HaveProductBean> notPayList = null;
		try {// 获取未下单数据
			notPayList = dbManager.selector(HaveProductBean.class)
					.where("lsh", "LIKE", "%" + mOpCurTable.getLsh() + "%")
					.findAll();
			FrameLog.v(TAG, "notPayList.size()" + notPayList.size());
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (notPayList != null && notPayList.size() > 0) {
			productList.addAll(notPayList);
			productAdapter.setItems(productList);
			productAdapter.notifyDataSetChanged();
			statisticsDishNum();
			doNoPayTotal();// 价格合计
		}
		if (!TextUtils.isEmpty(tasteStr)) { // 有整单备注数据
			mMiddleRl.setVisibility(View.VISIBLE);
			mRemarkRelalout.setVisibility(View.VISIBLE);
			mMarksTv.setText(tasteStr);
		} else {
			mMiddleRl.setVisibility(View.VISIBLE);
			mRemarkRelalout.setVisibility(View.VISIBLE);
			mMarksTv.setText("");
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		MobclickAgent.onPageStart(TAG);
	}

	@Override
	protected String getTitle() {
		return getString(R.string.take_dish);
	}

	@Override
	protected int getMenuResId() {
		return R.drawable.bg_menu_order_dish;
	}

	@Override
	public boolean onKeyDown(int keyCode) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			SysWorkTools.closeKeyboard(mSearchEt);
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
	public void OnRightMenuClick() {
		// 进入分类点菜
		AddProductFragment fragment = new AddProductFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constans.KEY_TABLE_INFO, mOpCurTable);
		fragment.setArguments(bundle);
		((MainActivity) getActivity()).entrySubFragment(fragment);
		SysWorkTools.closeKeyboard(iv_addRemark);
	}

	/**
	 * 搜索按钮事件
	 */
	private OnClickListener mSearchBtnListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mSearchEt.setText("");
		}
	};

	/**
	 * 显示完整文字
	 */
	private OnClickListener mShowMarkListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (bCanOpen) {
				mMarksTv.setMaxLines(10);
				mMarksTv.requestLayout();
				mShowMarksIv.setImageResource(R.drawable.up_marks);
				bCanOpen = false;
			} else {
				mMarksTv.setMaxLines(1);
				mMarksTv.requestLayout();
				mShowMarksIv.setImageResource(R.drawable.down_marks);
				bCanOpen = true;
			}
		}
	};

	/**
	 * 整单备注
	 */
	private OnClickListener mWholeRemarkListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			WholeDishRemarkFragment fragment = new WholeDishRemarkFragment();
			Bundle bundle = new Bundle();
			bundle.putString(Constans.KEY_REMARK, tasteStr);
			bundle.putString("lsh", mOpCurTable.getLsh());
			fragment.setArguments(bundle);
			((MainActivity) getActivity()).entrySubFragment(fragment);
		}
	};

	/**
	 * 确认下单
	 */
	private OnClickListener mSureTakeOrderLstener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			JSONArray jsonArray = new JSONArray();
			if (!productList.isEmpty()) {
				for (HaveProductBean hpBean : productList) {
					if (hpBean.getOrderState() == 0) {
						jsonArray.add(new DCaiBean(hpBean));
					}
				}
			} else {
				SysWorkTools.showToast(getActivity(), "请选择菜品");
				return;
			}
			if (null == tasteStr) {
				tasteStr = "";
			}
			orderDish(mOpCurTable.getFloorcode(), mOpCurTable.getCode(),
					mOpCurTable.getLsh(), tasteStr, jsonArray.toJSONString());
		}
	};

	/**
	 * 搜索功能监听
	 */
	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (!TextUtils.isEmpty(s.toString())) {
				mTongjiLinlout.setVisibility(View.GONE);
				mSearchIv.setVisibility(View.VISIBLE);
				mSearchRl.setVisibility(View.VISIBLE);
				mCancelSearchTv.setVisibility(View.VISIBLE);
				String searchText = s.toString();
				if (!TextUtils.isEmpty(searchText)) {
					String columnName = "status";
					try {
						pinyinList = dbManager
								.selector(Canyin_Shop_Product.class)
								.where(WhereBuilder.b("mcode", "LIKE",
										"%" + searchText + "%").and(
										WhereBuilder.b(columnName, "=", 0))).findAll();
						// .or(columnName, "=", 1)
						
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				pinyinAdapter.setItems(pinyinList);
				pinyinAdapter.notifyDataSetChanged();

			} else {
				pinyinList.clear();
				mSearchIv.setVisibility(View.GONE);
				mSearchRl.setVisibility(View.GONE);

			}

		}

	};

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	OnItemClickListener pinyinItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectProduct = (Canyin_Shop_Product) parent
					.getItemAtPosition(position);
			HaveProductBean bean = new HaveProductBean(selectProduct,
					mOpCurTable);

			productList.add(bean, 1);
			productAdapter.setItems(productList);
			productAdapter.notifyDataSetChanged();

			pinyinList.clear();
			mSearchEt.setText("");
			statisticsDishNum();
			doNoPayTotal();// 价格合计
			mSearchRl.setVisibility(View.GONE);
			mSearchIv.setVisibility(View.GONE);
			mCancelSearchTv.setVisibility(View.GONE);
			SysWorkTools.closeKeyboard(mSearchEt);
		}
	};

	/**
	 * 取消按钮事件
	 */
	OnClickListener cancelSearchListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mCancelSearchTv.setVisibility(View.GONE);
			mSearchIv.setVisibility(View.GONE);
			mSearchRl.setVisibility(View.GONE);
			mSearchEt.setText("");
			SysWorkTools.closeKeyboard(mSearchEt);
		}
	};

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
				delDish(position);
				break;
			}
			return false;
		}
	};

	/**
	 * 未下单合计
	 */
	private void doNoPayTotal() {

		double t = 0.0;
		for (int i = 0; i < productList.size(); i++) {
//			if (productList.get(i).getOrderState() == 0) {// 未下单 不是增菜
				noPayPrice = productList.get(i).getPrice()
						* productList.get(i).getCnt();
				t = t + noPayPrice;
//			}
		}

		DecimalFormat decimalFormat = new DecimalFormat("0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
		String distanceString = decimalFormat.format(t);
		mPriceTotalTv.setText("￥" + distanceString + "");
		noPayPrice = 0.0d;
	}

	/**
	 * 保存未下单的数据
	 */
	public void saveProduct() {

		try {
			if (mOpCurTable.getIsroom() != 5) {// 判断当前桌位是否为外卖台，是外卖则不需要存取数据库

				String columnName = "lsh";
				dbManager.delete(HaveProductBean.class,
						WhereBuilder.b(columnName, "=", mOpCurTable.getLsh()));
				if (null != productList && productList.size() > 0) {
					for (int i = 0; i < productList.size(); i++) {
						if (productList.get(i).getOrderState() == 0) {
							dbManager.save(productList.get(i));
						}
					}
					PrefsConfig.saveRemarks(mOpCurTable.getLsh(), tasteStr);
					
				}

			}
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	
	private void mToast() {
		// TODO Auto-generated method stub
		Toast t = new Toast(getActivity());
		t.setDuration(Toast.LENGTH_SHORT);
		View view =getActivity().getLayoutInflater().inflate(R.layout.layout_toast, (ViewGroup) getActivity().findViewById(R.id.toast_layout_root));
		t.setGravity(Gravity.CENTER, 0, 0);
		t.setMargin(40, 0);
		t.setView(view);
		t.show();
	}
	/**
	 * 单桌点菜
	 * 
	 * @param tingbh
	 * @param taibh
	 * @param ktlsh
	 * @param data
	 */
	private void orderDish(String tingbh, String taibh, String ktlsh,
			String taste, String data) {
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fetchOrderDishes(tingbh, taibh, ktlsh,
					taste, data, new FetchListener() {

						@Override
						public void onPreFetch() {
							bIsLoading = true;
							showLoading(true);
						}

						@SuppressLint("ShowToast")
						@SuppressWarnings("unchecked")
						@Override
						public void onPostFetch(int status, Object... result) {
							bIsLoading = false;
							showLoading(false);
							if (status == FetchListener.STATUS_OK) {
								SysWorkTools.showToast(getActivity(), "下单成功");
								productList.clear();
								try {
									dbManager.delete(
											HaveProductBean.class,
											WhereBuilder.b("lsh", "=",
													mOpCurTable.getLsh()));
								} catch (DbException e) {
									e.printStackTrace();
								}
								PrefsConfig.clearRemarks(mOpCurTable.getLsh());
								close();
								HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_SUCCESS_ORDER_DISH, "");
								
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		HomeBroadcastReceiver.unregisterIDataTransfer(this);
		saveProduct();
		SysWorkTools.closeKeyboard(mSearchEt);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onDataTransfer(int type, Object... data) {

		if (type == Constans.TYPE_WHOLE_TASTE) {
			tasteStr = (String) data[0];
			FrameLog.e(TAG, tasteStr + "*****************");
			if (!TextUtils.isEmpty(tasteStr)) { // 有整单备注数据
				mMiddleRl.setVisibility(View.VISIBLE);
				mRemarkRelalout.setVisibility(View.VISIBLE);
				mMarksTv.setText(tasteStr);
			} else {
				mMiddleRl.setVisibility(View.VISIBLE);
				mRemarkRelalout.setVisibility(View.VISIBLE);
				mMarksTv.setText("");
			}
		} else if (type == Constans.TYPE_SINGLE_TASTE) {
			int position = (Integer) data[0];
			String taste = (String) data[1];
			String wStatus = (String) data[2];
			if (!TextUtils.isEmpty(taste)) {
				productList.get(position).setMarkTaste(taste);
			}
			productList.get(position).setWaitStatus(wStatus);
			productAdapter.setItems(productList);
			productAdapter.notifyDataSetChanged();
		} else if (type == Constans.TYPE_ORDER_PRO_ADD_SUB) {
			productAdapter.notifyDataSetChanged();
			statisticsDishNum();
			doNoPayTotal();
		} else if (type == Constans.TYPE_ADD_PRO_LIST) {
			ProductList<HaveProductBean> addList = (ProductList<HaveProductBean>) data[0];
			for (HaveProductBean item : addList) {
				productList.add(item, 1);
			}
			productAdapter.setItems(productList);
			productAdapter.notifyDataSetChanged();
			statisticsDishNum();
			doNoPayTotal();
		} else if (type == Constans.TYPE_INPUT_PRO_CNT) {
			float fCnt = (Float) data[0];
			int positon = (Integer) data[1];
			productList.get(positon).setCnt(fCnt);
			productAdapter.notifyDataSetChanged();

			statisticsDishNum();
			doNoPayTotal();
		}

	}

	private void delDish(int position) {
		productList.remove(position);// 删除当前位置数据
		productAdapter.notifyDataSetChanged();
		statisticsDishNum();
		doNoPayTotal();// 调用未下单合计
	}

	private void statisticsDishNum() {
		if (productList.size() > 0) { // 品项目合计
			List<String> mList = new ArrayList<String>();
			for (int i = 0; i < productList.size(); i++) {// 获取到要比较的字段
				mList.add(productList.get(i).getCode());
			}
			for (int i = 0; i < mList.size(); i++) {// 比较是否有相同数据

				int j = mList.lastIndexOf(mList.get(i));

				if (i != j) {
					mList.remove(j);
					i--;// 每次有重复都让i回到初始位置，i==j时才可以向下循环
				}
			}
			mTongjiLinlout.setVisibility(View.VISIBLE);
			mProductPhaseTotalTv.setText(mList.size() + "项");
			float count = 0.0f;
			for (HaveProductBean item : productList) {
				count += item.getCnt();
			}
			try {
				mNumTotalTv.setText(Constans.DF.format(count));
			} catch (Exception e) {
				FrameLog.e(TAG, e);
			}
		} else {

			mTongjiLinlout.setVisibility(View.GONE);
			mProductPhaseTotalTv.setText("0项");
			mNumTotalTv.setText("0");
		}
	}

	public void entrySingleDishRemark(int position, HaveProductBean product) {
		SingleDishRemarkFragment fragment = new SingleDishRemarkFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Constans.TAKE_ORDER_POSITION, position);
		bundle.putString(Constans.WAIT_STATUS_TASTE, product.getWaitStatus());
		fragment.setArguments(bundle);
		((MainActivity) getActivity()).entrySubFragment(fragment);
	}

}