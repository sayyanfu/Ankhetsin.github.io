package com.fangbian365.kuaidi.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.CHBApp;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Diningtable;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Floor;
import com.fangbian365.kuaidi.base.bean.TableStatusChange;
import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.mod.asynctask.http.FetchListener;
import com.fangbian365.kuaidi.mod.manager.DataFetchManager;
import com.fangbian365.kuaidi.ui.adapter.TabsAdapter;
import com.fangbian365.kuaidi.ui.fragment.AddProductFragment;
import com.fangbian365.kuaidi.ui.fragment.AllTableFragment;
import com.fangbian365.kuaidi.ui.fragment.BillFragment;
import com.fangbian365.kuaidi.ui.fragment.ChangeTableDialogFragment;
import com.fangbian365.kuaidi.ui.fragment.ExitAccountFragment;
import com.fangbian365.kuaidi.ui.fragment.HeaderFragment;
import com.fangbian365.kuaidi.ui.fragment.IdleTableFragment;
import com.fangbian365.kuaidi.ui.fragment.InuseTableFragment;
import com.fangbian365.kuaidi.ui.fragment.MsgFragment;
import com.fangbian365.kuaidi.ui.fragment.OpenTableFragment;
import com.fangbian365.kuaidi.ui.fragment.SettingFragment;
import com.fangbian365.kuaidi.ui.fragment.TableDetailFragment;
import com.fangbian365.kuaidi.ui.receiver.HomeBroadcastReceiver;
import com.fangbian365.kuaidi.ui.receiver.IReceiver;
import com.fangbian365.kuaidi.ui.uisupport.PickerView;
import com.fangbian365.kuaidi.ui.uisupport.ScllorTabView;
import com.fangbian365.kuaidi.ui.uisupport.PickerView.onSelectListener;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by anqi on 16/1/10.
 */
public class MainActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener, IReceiver,
		OnCheckedChangeListener {
	private static final String TAG = "MainActivity";

	private TextView mTvShopName;// tv_shop_name
	private TextView mTvWaiterName;// tv_waiter_name
	//private ImageView mIvLogout; // iv_logout
	//private TextView mTvUser; // tv_user_bar
	
	private RelativeLayout mLayoutSearchBar;
	
	private Animation animation;
	private ImageView mIvMsgTag;
	
	private PickerView mHallPicker;//picker_hall

	private RadioGroup mRgPagerTab;
	private RadioButton mRbAll;
	private RadioButton mRbIdle;
	private RadioButton mRbInuse;

	private EditText mEtSearch;// et_search
	private ImageView mIvSearch;
	private ViewPager mTablePager;// table_page

	private View mBg;
	private ViewGroup mVgPicLayer;
	private int popLayerHeight;
	private ImageView mIvKaitai;
	private ImageView mIvQingtai;
	private ImageView mIvDiandan;
	private ImageView mIvHuantai;
	//private ImageView mIvMsg;

	//新增
	private ImageView IV_setting;
	private ImageView iv_msg;//推送消息
	private TextView tv_msg;//推送提示
	private ImageView iv_jiezhang;//结账
	
	private static ViewGroup mFrameViews = null; // 二级以上Fragment
	private ScllorTabView mTabLine;// iv_off_tab_line
	
	private RelativeLayout mLayoutLoading;
	private ImageView mIvLoading;
	private TextView mTvLoading;
	private Animation hyperspaceJumpAnimation;

	private TabsAdapter mTabsAdapter;
	private int curPage = 0;
	private boolean bIsSearchStatus = false;

	private DbManager dbManager;

	private boolean bSync = false;
	private List<Canyin_Shop_Diningtable> mTableList;

	public List<Canyin_Shop_Diningtable> getCurTableList() {
		return mTableList;
	}

	private List<Canyin_Shop_Floor> hList;
	private String curHall = "全部";

//	public String getCurHall() {
//		return curHall;
//	}
	private Canyin_Shop_Diningtable mOpCurTable;
	private SoundPool mSoundPool;
	private Map mSoundPoolMap;
	private int mVolume;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 用来获取屏幕高宽
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
		int hight = wm.getDefaultDisplay().getHeight();// 获取屏幕高度

		FrameLog.e("MyMainacitivity", "--------------------------------------" + hight
				+ "  ------   " + width);
		
		dbManager = CHBApp.getInstance().getDbManager();

		mTvShopName = (TextView) findViewById(R.id.tv_shop_name);
		mTvShopName.setText(PrefsConfig.ShopName);
		mTvWaiterName = (TextView) findViewById(R.id.tv_waiter_name);
		FrameLog.d("AAAA", "4444444" + PrefsConfig.workerName);
		iv_msg = (ImageView) findViewById(R.id.iv_msg);
		iv_msg.setOnClickListener(this);
		tv_msg = (TextView) findViewById(R.id.TV_msg);
		
		mTvWaiterName.setOnClickListener(this);
		if (null == PrefsConfig.workerName) {
			mTvWaiterName.setText("");
		} else {
			mTvWaiterName.setText("(" + PrefsConfig.workerName + ")");
		}
		//mIvLogout = (ImageView) findViewById(R.id.iv_logout);
		//mIvLogout.setOnClickListener(this);
		IV_setting = (ImageView) findViewById(R.id.iv_setting);
		IV_setting.setOnClickListener(this);
		
//		mTvUser = (TextView) findViewById(R.id.tv_user_bar);
//		mTvUser.setText("欢迎您, " + PrefsConfig.workerName + " 登录小二筷点");
//		mTvUser.setOnClickListener(this);
		mFrameViews = (ViewGroup) findViewById(R.id.main_frame);
		
//		mIvMsgTag = (ImageView) findViewById(R.id.iv_msg_tag);
//		setFlickerAnimation(mIvMsgTag);
		mLayoutSearchBar = (RelativeLayout) findViewById(R.id.layout_search_bar);
		
		mHallPicker = (PickerView) findViewById(R.id.picker_hall);
		mHallPicker.setOnSelectListener(mOnHallSelectorListener);
//		mHallPicker.performSelect();

		mRgPagerTab = (RadioGroup) findViewById(R.id.rg_viewpager_tab);
		mRbAll = (RadioButton) findViewById(R.id.rb_all_table);
		mRbIdle = (RadioButton) findViewById(R.id.rb_idle_table);
		mRbInuse = (RadioButton) findViewById(R.id.rb_inuse_table);
		mRgPagerTab.setOnCheckedChangeListener(this);
		mTabLine = (ScllorTabView) findViewById(R.id.iv_off_tab_line);
		mTabLine.setTabNum(3);
		mTabLine.setCurrentNum(0);
		mTabLine.setSelectedColor(0XffFA920B);

		mEtSearch = (EditText) findViewById(R.id.et_search);
		mIvSearch = (ImageView) findViewById(R.id.iv_search_tag);
		mIvSearch.setOnClickListener(this);
		mTablePager = (ViewPager) findViewById(R.id.table_view_pager);
		mTablePager.setOffscreenPageLimit(3);
		mTablePager.setOnPageChangeListener(this);

		mBg = findViewById(R.id.pop_background);
		mBg.setOnClickListener(this);
		mVgPicLayer = (ViewGroup) findViewById(R.id.pop_table_op);
		popLayerHeight = mVgPicLayer.findViewById(R.id.pop_root).getLayoutParams().height;
		FrameLog.d(TAG, "Pop Layer Height = " + popLayerHeight);
		mIvKaitai = (ImageView) mVgPicLayer.findViewById(R.id.iv_kaitai);
		mIvQingtai = (ImageView) mVgPicLayer.findViewById(R.id.iv_qingtai);
		mIvDiandan = (ImageView) mVgPicLayer.findViewById(R.id.iv_diandan);
		mIvHuantai = (ImageView) mVgPicLayer.findViewById(R.id.iv_huantai);
//		mIvMsg = (ImageView) mVgPicLayer.findViewById(R.id.iv_xiaoxi);
		iv_jiezhang = (ImageView) mVgPicLayer.findViewById(R.id.iv_jiezhang);
		mIvKaitai.setOnClickListener(this);
		mIvQingtai.setOnClickListener(this);
		mIvDiandan.setOnClickListener(this);
		mIvHuantai.setOnClickListener(this);
//		mIvMsg.setOnClickListener(this);
		iv_jiezhang.setOnClickListener(this);
		
		mLayoutLoading = (RelativeLayout) findViewById(R.id.layout_loading);
		mLayoutLoading.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		mIvLoading = (ImageView) findViewById(R.id.iv_loading);
		mTvLoading = (TextView) findViewById(R.id.tv_loading);
		hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.loading_animation);  

		mTabsAdapter = new TabsAdapter(this);
		mTabsAdapter.addTab("All", AllTableFragment.class);
		mTabsAdapter.addTab("Idle", IdleTableFragment.class);
		mTabsAdapter.addTab("Inuse", InuseTableFragment.class);

		mTablePager.setAdapter(mTabsAdapter);

		registerReceiver();
		doFetchHallandTableInfo();
		initSoundResource();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		FrameLog.d("AAAA", "onNewIntent");
		if (null == PrefsConfig.workerName) {
			mTvWaiterName.setText("");
		} else {
			mTvWaiterName.setText("(" + PrefsConfig.workerName + ")");
		}
		
//		mTvUser.setText("欢迎您, " + PrefsConfig.workerName + " 登录小二筷点");
		mTvShopName.setText(PrefsConfig.ShopName);
		
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		FrameLog.d("AAAA", "onRestoreInstanceState");
		
	}
	

	private void initSoundResource() {
		
		AudioManager mgr = (AudioManager) MainActivity.this.getSystemService(Context.AUDIO_SERVICE);
		// 初始化soundPool 对象,第一个参数是允许有多少个声音流同时播放,第2个参数是声音类型,第三个参数是声音的品质
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mSoundPoolMap.put(0, mSoundPool.load(MainActivity.this, R.raw.tip_msg, 1));
		mVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		FrameLog.d("AAAA", "onResume");
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		FrameLog.d("AAAA", "onPause");
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		FrameLog.d("AAAA", "onStop");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		FrameLog.d("AAAA", "onDestroy");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		FrameLog.d("AAAA", "onSaveInstanceState");
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		FrameLog.d("AAAA", "onStart");
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		FrameLog.d("AAAA", "onRestart");
		try {
			if(mTableList == null || mTableList.size() <= 0) {
				mTvWaiterName.setText("(" + PrefsConfig.workerName + ")");
//				mTvUser.setText("欢迎您, " + PrefsConfig.workerName + " 登录小二筷点");
				mTvShopName.setText(PrefsConfig.ShopName);
				
				doFetchHallandTableInfo();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void registerReceiver() {
		// 注册广播
		HomeBroadcastReceiver.registerFragmentReceiver(this);
	}

	private void doFetchHallandTableInfo() {
		if (SysWorkTools.isNetAvailable()) {
			// 楼层信息获取
			DataFetchManager.getInstance().fetchHallInfo(new FetchListener() {
				@Override
				public void onPreFetch() {
					showLoading(true);
				}

				@SuppressWarnings("unchecked")
				@Override
				public void onPostFetch(int status, Object... result) {
					showLoading(false);
					if (status == FetchListener.STATUS_OK) {
						hList = (List<Canyin_Shop_Floor>) result[0];
						try {
							if (hList != null) {
								FrameLog.d(TAG, "******" + hList.size());
								dbManager.delete(Canyin_Shop_Floor.class);
								for (int i = 0; i < hList.size(); i++) {// 依次写入楼层表数据
									Canyin_Shop_Floor floorBean = hList.get(i);
									dbManager.save(floorBean);
								}
							}
							doFetchTableInfo();
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

					} else {
						String msg = (String)result[0];
						SysWorkTools.showToast(MainActivity.this, msg);
					}
				}
			});
		}else {
			SysWorkTools.showToast(MainActivity.this, "请检查网络");
		}
	

	}
	
	private onSelectListener mOnHallSelectorListener = new onSelectListener() {
		
		@Override
		public void onSelect(Canyin_Shop_Floor floor) {
			curHall = floor.getFloorcode();
			if(curHall.equals("全部")) {
				mLayoutSearchBar.setVisibility(View.VISIBLE);
				mEtSearch.setText("");
			} else {
				mLayoutSearchBar.setVisibility(View.GONE);
				SysWorkTools.closeKeyboard(mLayoutSearchBar);
			}
			if(mTableList != null && mTableList.size() > 0) {
				HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_TABLE_CHANGED, mTableList, curHall, false);
			}
		}
	};
	
	public void doFetchTableInfo() {
		if (SysWorkTools.isNetAvailable()) {
			// 桌位信息获取
			DataFetchManager.getInstance().fetchTableInfo("", "",
					new FetchListener() {

						@Override
						public void onPreFetch() {
							showLoading(true);
						}

						@SuppressWarnings("unchecked")
						@Override
						public void onPostFetch(int status, Object... result) {
							showLoading(false);
							if (status == FetchListener.STATUS_OK) {
								List<Canyin_Shop_Diningtable> tList = (List<Canyin_Shop_Diningtable>) result[0];
								if (tList != null) {
									try {
										dbManager.delete(Canyin_Shop_Diningtable.class);
										for (int i = 0; i < tList.size(); i++) {
											Canyin_Shop_Diningtable tableBean = tList
													.get(i);
											dbManager.save(tableBean);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									refreshUI(tList);
								}
							} else {
								String msg = (String)result[0];
								SysWorkTools.showToast(MainActivity.this, msg);
							}

							if (!bSync) {
								if (SysWorkTools.isNetAvailable()) {
									// 口味信息同步
									DataFetchManager.getInstance().fetchTaste(null);
									// 退菜说明
									DataFetchManager.getInstance().fetchBackFood(null);
									// 赠菜原因
									DataFetchManager.getInstance().fetchGiveFoodReason(null);
									// 菜品信息获取
									DataFetchManager.getInstance().fetchProductInfo(null);
									// 菜品分类信息获取
									DataFetchManager.getInstance().fetchProductTypeInfo(null);
									// 餐段信息同步
									DataFetchManager.getInstance().fetchMealPeriod(null);
								}else {
									SysWorkTools.showToast(MainActivity.this, "请检查网络");
								}
								
							}
							bSync = true;
						}
					});
		}else{
			SysWorkTools.showToast(MainActivity.this, "请检查网络");
		}
		
	}

	private void refreshUI(List<Canyin_Shop_Diningtable> dataList) {
		if (dataList == null || dataList.size() <= 0) {
			return;
		}
		try {
			for (Canyin_Shop_Diningtable table : dataList) {
				Canyin_Shop_Floor item = dbManager
						.selector(Canyin_Shop_Floor.class)
						.where("floorcode", "=", table.getFloorcode())
						.findFirst();
				table.setFloorName(item.getFloorname());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		mTableList = dataList;
		
		HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_TABLE_CHANGED, mTableList, curHall, false);
	}

	@Override
	public void onClick(View v) {
		Fragment f;
		DialogFragment dialog;
		Bundle bundle;
		switch (v.getId()) {
		case R.id.pop_background:
			hidePopupMenu();
			break;
		case R.id.iv_kaitai:
			hidePopupMenu();
			f = new OpenTableFragment();
			bundle = new Bundle();
			bundle.putSerializable(Constans.KEY_TABLE_INFO, mOpCurTable);
			f.setArguments(bundle);
			entrySubFragment(f);

			break;
		case R.id.iv_qingtai:
			hidePopupMenu();
			changeTableStatus(Constans.TABLE_IDLE, mOpCurTable);

			break;
		case R.id.iv_diandan:
			hidePopupMenu();
			f = new TableDetailFragment();
			bundle = new Bundle();
			bundle.putSerializable(Constans.KEY_TABLE_INFO, mOpCurTable);
			f.setArguments(bundle);
			entrySubFragment(f);
			break;
		case R.id.iv_huantai:
			hidePopupMenu();
			dialog = new ChangeTableDialogFragment();
			bundle = new Bundle();
			bundle.putSerializable(Constans.PARAM_TABLE_OLD, mOpCurTable);

			dialog.setArguments(bundle);
			dialog.show(getSupportFragmentManager(), "ChangeTableDialogFragment");
			break;
		case R.id.iv_jiezhang:
			hidePopupMenu();
			f = new BillFragment();
			bundle = new Bundle();
			bundle.putSerializable(Constans.KEY_BILL, mOpCurTable);
			f.setArguments(bundle);
			entrySubFragment(f);
			break;
//		case R.id.iv_xiaoxi:
//			hidePopupMenu();
//			f = new MsgFragment();
//			entrySubFragment(f);
//			break;
		case R.id.iv_msg:
			hidePopupMenu();
			f = new MsgFragment();
			entrySubFragment(f);
			tv_msg.setVisibility(View.GONE);
			SysWorkTools.closeKeyboard(tv_msg);
			break;
//		case R.id.tv_waiter_name: // 退出账户
//			dialog = new ExitAccountFragment();
//			FragmentManager fragmentManager = getSupportFragmentManager();
//			dialog.show(fragmentManager, "ExitAccountFragment");
//			break;
		case R.id.iv_setting:
			f = new SettingFragment();
			entrySubFragment(f);
			SysWorkTools.closeKeyboard(tv_msg);
			break;
//		case R.id.tv_user_bar:
//			f = new MsgFragment();
//			entrySubFragment(f);
//			mTvUser.setText("欢迎您, " + PrefsConfig.workerName + " 登录小二筷点");
//			animation.cancel();
//			mIvMsgTag.setVisibility(View.GONE);
//			break;
		case R.id.iv_search_tag:
			SysWorkTools.closeKeyboard(mEtSearch);
			String searchBH = mEtSearch.getText().toString().trim();
			if(TextUtils.isEmpty(searchBH)) {
				return;
			}
			if(mTableList == null || mTableList.size() <= 0) {
				doFetchHallandTableInfo();
				return;
			}
			
			String columnName = "code";
			List<Canyin_Shop_Diningtable> tList = null;
			try {
				tList = dbManager.selector(Canyin_Shop_Diningtable.class)
						.where(columnName, "LIKE", "%" + searchBH + "%")
						.findAll();
				FrameLog.v(TAG, "查询结果 " + tList.size());
			} catch (DbException e) {
				e.printStackTrace();
			}
			
			if(tList != null && !tList.isEmpty()) {
				bIsSearchStatus = true;
				HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_TABLE_SEARCH, curPage, tList);
			} else {
				SysWorkTools.showToast(this, "没有搜到指定桌位");
			}
			
			break;	
		default:
			break;
		}
	}

	/**
	 * 改变Table状态属性
	 */
	private void changeTableStatus(int tStatus, Canyin_Shop_Diningtable table) {
		TableStatusChange data = new TableStatusChange();
		data.setTingbh(table.getFloorcode());
		data.setTaibh(table.getCode());
		data.setState(tStatus);

		JSONArray jsonArray = new JSONArray();
		jsonArray.add(data);
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fetchStopOpenTable(jsonArray.toJSONString(), new FetchListener() {

				@Override
				public void onPreFetch() {
					showLoading(true);
				}

				@Override
				public void onPostFetch(int status, Object... result) {
					showLoading(false);
					if (status == FetchListener.STATUS_OK) {
						SysWorkTools.showToast(MainActivity.this, "清台成功");
						doFetchTableInfo();
					} else {
						SysWorkTools.showToast(MainActivity.this, "清台失败");
					}
				}
			});
		}else{
			SysWorkTools.showToast(MainActivity.this, "请检查网络");
		}

	}
	
	public void entrySubFragment(Fragment fragment) {
		if (fragment != null/* && fragment instanceof HeaderFragment */) {
			String tag = fragment.getClass().getSimpleName();
			FrameLog.d(TAG, "jumpTo:" + tag);

			mFrameViews.setVisibility(View.VISIBLE);
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.setCustomAnimations(R.anim.slide_in_from_right,
					R.anim.slide_out_from_right, R.anim.slide_in_from_right,
					R.anim.slide_out_from_right);
			transaction.add(R.id.main_frame, fragment, tag);
			transaction.addToBackStack(tag); // 添加回退栈
			transaction.commitAllowingStateLoss();
		}
	}
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	FragmentManager fragmentManager = getSupportFragmentManager();
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			int count = fragmentManager.getBackStackEntryCount();
			if (count > 0) {
				List<Fragment> fList = fragmentManager.getFragments();
				Fragment f = null;
				for (int i = fList.size(); i > 0; i--) {
					if (fList.get(i - 1) != null) {
						f = fList.get(i - 1);
						break;
					}
				}

				if (f instanceof HeaderFragment) {
					boolean neetTip = ((HeaderFragment) f).onKeyDown(keyCode);
					if (neetTip)
						return true;
				}
				
				if(f instanceof AddProductFragment) {
					boolean neetTip = ((AddProductFragment) f).onKeyDown(keyCode);
					if (neetTip)
						return true;
				}

				fragmentManager.popBackStack();
				return true;
			} else {
				moveTaskToBack(true);
//				ExitAccountFragment f = new ExitAccountFragment();
//				f.show(getSupportFragmentManager(), "ExitAccountFragment");
//				return true;
			}
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
    }

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
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		mTabLine.setOffset(position, positionOffset);
	}

	@Override
	public void onPageSelected(int position) {
		curPage = position;
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
		mEtSearch.setText("");
		
		if(bIsSearchStatus) {
			HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_TABLE_CHANGED, mTableList, curHall, false);
			bIsSearchStatus = false;
		}
	}

	public void showPopupMenu(Canyin_Shop_Diningtable table) {
		SysWorkTools.closeKeyboard(mEtSearch);
		mEtSearch.setText("");
		mOpCurTable = table;

		int status = table.getStatus();
		switch (status) {
		case 0:// 空闲
			mIvKaitai.setVisibility(View.VISIBLE);
			mIvKaitai.setImageResource(R.drawable.selector_bg_table_kt);
			mIvKaitai.setClickable(true);
			
			mIvQingtai.setVisibility(View.GONE);
			mIvDiandan.setImageResource(R.drawable.bg_table_dd_disable);
			mIvDiandan.setClickable(false);
			mIvHuantai.setImageResource(R.drawable.bg_table_ht_disable);
			mIvHuantai.setClickable(false);
			iv_jiezhang.setImageResource(R.drawable.bg_nouse_jiezhang);
			iv_jiezhang.setClickable(false);
			break;
		case 1:// 占用
			mIvKaitai.setVisibility(View.VISIBLE);
			mIvKaitai.setClickable(false);
			mIvKaitai.setImageResource(R.drawable.bg_table_kt_disable);

			mIvQingtai.setVisibility(View.GONE);
			mIvDiandan.setImageResource(R.drawable.selector_bg_table_dd);
			mIvDiandan.setClickable(true);
			mIvHuantai.setImageResource(R.drawable.selector_bg_table_ht);
			mIvHuantai.setClickable(true);
			iv_jiezhang.setImageResource(R.drawable.bg_nochose_jiezhnag);
			iv_jiezhang.setClickable(true);
			break;
		case 2:// 禁用
			mIvKaitai.setVisibility(View.VISIBLE);
			mIvKaitai.setClickable(false);
			mIvKaitai.setImageResource(R.drawable.bg_table_kt_disable);
			mIvQingtai.setVisibility(View.GONE);

			mIvDiandan.setImageResource(R.drawable.bg_table_dd_disable);
			mIvDiandan.setClickable(false);
			mIvHuantai.setImageResource(R.drawable.bg_table_ht_disable);
			mIvHuantai.setClickable(false);
			iv_jiezhang.setImageResource(R.drawable.bg_nouse_jiezhang);
			iv_jiezhang.setClickable(false);
			break;
		case 3:// 待清
			mIvKaitai.setVisibility(View.GONE);
			mIvQingtai.setVisibility(View.VISIBLE);

			mIvDiandan.setImageResource(R.drawable.bg_table_dd_disable);
			mIvDiandan.setClickable(false);
			mIvHuantai.setImageResource(R.drawable.bg_table_ht_disable);
			mIvHuantai.setClickable(false);
			iv_jiezhang.setImageResource(R.drawable.bg_nouse_jiezhang);
			iv_jiezhang.setClickable(false);
			break;
		default:
			break;
		}
		mVgPicLayer.setVisibility(View.VISIBLE);
		TranslateAnimation animation = new TranslateAnimation(0, 0, popLayerHeight, 0);
		animation.setDuration(200);
		mVgPicLayer.startAnimation(animation);

		mBg.setVisibility(View.VISIBLE);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1.0f);
		alphaAnimation.setDuration(200);
		mBg.startAnimation(alphaAnimation);
	}
	
	public void showLoading(boolean isShow) {
		if(isShow) {
			mLayoutLoading.setVisibility(View.VISIBLE);
			mIvLoading.startAnimation(hyperspaceJumpAnimation);
		} else {
			mIvLoading.clearAnimation();
			mLayoutLoading.setVisibility(View.GONE);
		}
	}
	
	private void hidePopupMenu() {
		for (Canyin_Shop_Diningtable item : mTableList) {
			item.setChoosed(false);
		}
		HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_TABLE_CHANGED, mTableList, curHall, false);

		TranslateAnimation animation = new TranslateAnimation(0, 0, 0, popLayerHeight);
		animation.setDuration(200);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				mVgPicLayer.setVisibility(View.GONE);
				mBg.setVisibility(View.GONE);
			}
		});

		mVgPicLayer.startAnimation(animation);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (!SysWorkTools.isNetAvailable()) {
//			SysWorkTools.showToast(MainActivity.this, "请检查网络");
			return;
		}
		if (action.equals(Constans.ACTION_RECEIVER_TAI_STATUS)) {
//			doFetchTableInfo();
			String tingBh = intent.getStringExtra("tingBh");
			String taiBh = intent.getStringExtra("taiBh");
			FrameLog.e("tingbh ----taibh", tingBh + ":::" + taiBh);
			doNewFecthTableInfo(tingBh, taiBh);
		} else if (action.equals(Constans.ACTION_RECEIVER_TING_STATUS)) {
			doFetchHallandTableInfo();
		} else if (action.equals(Constans.ACTION_RECEIVER_PRODUCT_EDIT)) {
			DataFetchManager.getInstance().fetchProductInfo(null);
		} else if (action.equals(Constans.ACTION_RECEIVER_PRODUCT_TYPE_EDIT)) {
			DataFetchManager.getInstance().fetchProductTypeInfo(null);
		} else if (action.equals(Constans.ACTION_RECEIVER_PRODUCT_TASTE_EDIT)) {
			DataFetchManager.getInstance().fetchTaste(null);
		} else if (action.equals(Constans.ACTION_RECEIVER_PRODUCT_STATUS)) {
			DataFetchManager.getInstance().fetchProductInfo(null);
		} else if (action.equals(Constans.ACTION_RECEIVER_MEAL_TIME)) {
			DataFetchManager.getInstance().fetchMealPeriod(null);
		} else if(action.equals(Constans.ACTION_RECEIVER_POS_LS_DC)) {
			tv_msg.setVisibility(View.VISIBLE);
			//animation.cancel();     
			//mIvMsgTag.setVisibility(View.VISIBLE);
			//animation.start();
//			mTvUser.setText("有新的送单信息！");
			if (PrefsConfig.login_status) {
				mSoundPool.play((Integer) mSoundPoolMap.get(0), mVolume, mVolume, 1, 0, 1f);
			}
		} else if(action.equals(Constans.ACTION_RECEIVER_LOGOUT)){
			String key = intent.getStringExtra("key");
			String opercode = intent.getStringExtra("opercode");
			FrameLog.e(TAG, PrefsConfig.workerNum + "*********" + opercode + "****" + PrefsConfig.key + "---" + key);
			if (PrefsConfig.workerNum.equals(opercode) && !PrefsConfig.key.equals(key)) { 
				logoutAccount();
				SysWorkTools.showToast(MainActivity.this, "您的帐户已在别的设备登录");
			}	
		} else if(action.equals(Constans.ACTION_RECEIVER_PRODUCT_RESON_TUI)) {
			// 退菜说明
			DataFetchManager.getInstance().fetchBackFood(null);
		} else if(action.equals(Constans.ACTION_RECEIVER_PRODUCT_RESON_ZENG)) {
			// 赠菜原因
			DataFetchManager.getInstance().fetchGiveFoodReason(null);
		}
	}
	
	private void setFlickerAnimation(ImageView ivMsgTag) {
		animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
		animation.setDuration(500); // duration - half a second
		animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
		animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
		animation.setRepeatMode(Animation.REVERSE); // 
		ivMsgTag.setAnimation(animation);
}

	/**
	 * 以下的几个方法用来，让fragment能够监听touch事件
	 */
	private List<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
			10);

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		for (MyOnTouchListener listener : onTouchListeners) {
			listener.onTouch(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
		onTouchListeners.add(myOnTouchListener);
	}

	public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
		onTouchListeners.remove(myOnTouchListener);
	}

	public interface MyOnTouchListener {
		public boolean onTouch(MotionEvent ev);
	}
	
	private void logoutAccount() {
		PrefsConfig.clearUser();
		Intent intent = new Intent(MainActivity.this, BindShopActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(Constans.LOGOUT, "logout");
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
	/**
	 * 获取推送桌位信息
	 */
	public void doNewFecthTableInfo(String tingbh, String taibh) {
		DataFetchManager.getInstance().fetchTableInfo(tingbh, taibh,
				new FetchListener() {

					@Override
					public void onPreFetch() {
					}

					@Override
					public void onPostFetch(int status, Object... result) {
						// TODO Auto-generated method stub
						if (status == FetchListener.STATUS_OK) {
							List<Canyin_Shop_Diningtable> newTList = (List<Canyin_Shop_Diningtable>) result[0];// 接受服务器返回数据
							List<Canyin_Shop_Diningtable> item = new ArrayList<Canyin_Shop_Diningtable>();// 获取本地服务器数据用于对比文件
							Canyin_Shop_Diningtable tableBean = null;// 单桌数据
							if (newTList != null) {
								try {
									item = dbManager.selector(Canyin_Shop_Diningtable.class).findAll();// 查找本地数据库桌位信息
									dbManager.delete(Canyin_Shop_Diningtable.class);// 查询完毕删除本地数据库

									for (int j = 0; j < item.size(); j++) {// 通过循环对比服务器返回的修改后的数据和本地数据库的数据，将改变的数据重新赋值；

										Canyin_Shop_Diningtable mItem = item.get(j);// 单桌数据

										for (int i = 0; i < newTList.size(); i++) {

											tableBean = newTList.get(i);// 服务器返回的数据

											// 查找出需要修改的数据，并将其修改
											if (tableBean.getFloorcode().equals(mItem.getFloorcode())
													&& tableBean.getCode().equals(mItem.getCode())) {
												mItem.setLsh(tableBean.getLsh());
												mItem.setStatus(tableBean.getStatus());
												mItem.setPersoncnt(tableBean.getPersoncnt());
												mItem.setPriceTotal(tableBean.getPriceTotal());
												mItem.setIsProduct(tableBean.getIsProduct());
												mItem.setIsroom(tableBean.getIsroom());
												mItem.setStarttime(tableBean.getStarttime());
												mItem.setFloorcode(tableBean.getFloorcode());
												mItem.setCode(tableBean.getCode());
												mItem.setMaxseat(tableBean.getMaxseat());
												mItem.setDtablename(tableBean.getDtablename());
											}
											dbManager.save(mItem);// 修改完毕重新报存所有桌位数据
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								refreshUI(item);// 刷新界面
							}
						} else {
							SysWorkTools.showToast(MainActivity.this, getString(R.string.fetch_table_info_false));
						}

					}
				});
	}
}
