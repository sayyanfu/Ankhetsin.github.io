package com.fangbian365.kuaidi.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.CHBApp;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Diningtable;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Floor;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Tcreson;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Zsreson;
import com.fangbian365.kuaidi.base.bean.GiveDishDataItemBean;
import com.fangbian365.kuaidi.base.bean.HaveProductBean;
import com.fangbian365.kuaidi.base.bean.PopBean;
import com.fangbian365.kuaidi.base.bean.TuiCai;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.mod.asynctask.http.FetchListener;
import com.fangbian365.kuaidi.mod.manager.DataFetchManager;
import com.fangbian365.kuaidi.ui.receiver.HomeBroadcastReceiver;
import com.fangbian365.kuaidi.ui.uisupport.ProgressView;
import com.fangbian365.kuaidi.ui.uisupport.SpinerPopWindow;
import com.fangbian365.kuaidi.ui.uisupport.SpinerPopWindow.IOnItemSelectListener;
import com.umeng.analytics.MobclickAgent;

public class ProductOperationDialogFragment extends DialogFragment implements
		OnClickListener {
	private static final String TAG = "ProductOperationFragment";
	private DbManager dbManager;
	private RelativeLayout mRootView;

	private TextView mTvProName;
	private TextView mTvProCnt;

	private EditText mEtGiftNum;
	private EditText mEtGiftReason;
	// private ImageView mIvDownTag;
	private SpinerPopWindow mSpGiftReason;
	private List<Canyin_Shop_Zsreson> give_list = null;

	private EditText mEtTcaiNum;
	private EditText mEtTcaiReason;
	private SpinerPopWindow mSpTcaiReason;
	private List<Canyin_Shop_Tcreson> tuicai_list = null;

	private TextView mTvTableName;
	private TextView mTvPopHall;
	private TextView mTvPopTable;
	private SpinerPopWindow mSpHall = null;
	private SpinerPopWindow mSpTable = null;
	private String newTingBh;// 厅编号
	private String newTaiBh;// 台边号
	private String newLsh;// 流水号

	private EditText mEtPersonNum;
	private EditText mEtWaiterCode;

	private int mType;
	private Canyin_Shop_Diningtable mTable;
	private HaveProductBean mProduct;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getArguments();
		if (bundle != null) {
			mType = bundle.getInt(Constans.KEY_PRO_DIALOG_TYPE);
			mProduct = (HaveProductBean) bundle
					.getSerializable(Constans.KEY_PRO_DETAIL);
			mTable = (Canyin_Shop_Diningtable) bundle
					.getSerializable(Constans.KEY_TABLE_INFO);
		}
		dbManager = CHBApp.getInstance().getDbManager();
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

	View view = null;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		
		if (mType == 1) {
			view = inflater.inflate(R.layout.product_zhuancai_dialog, null, false);
			mRootView = (RelativeLayout) view.findViewById(R.id.layout_root_view);
			view.findViewById(R.id.tv_sure).setOnClickListener(this);
			view.findViewById(R.id.tv_cancle).setOnClickListener(this);

			mTvTableName = (TextView) view.findViewById(R.id.tv_table_name);
			mTvProName = (TextView) view.findViewById(R.id.tv_pro_name);
			mTvProCnt = (TextView) view.findViewById(R.id.tv_pro_cnt);
			mTvPopHall = (TextView) view.findViewById(R.id.tv_hall);
			mTvPopTable = (TextView) view.findViewById(R.id.tv_table);
			mTvPopHall.setOnClickListener(this);
			mTvPopTable.setOnClickListener(this);

			mTvTableName.setText(mTable.getDtablename());
			mTvProName.setText(mProduct.getCodeName());
			mTvProCnt.setText(mProduct.getCnt() + mProduct.getUnits());

			initTingSp();

		} else if (mType == 2) {
			view = inflater.inflate(R.layout.product_tuicai_dialog, null, false);
			mRootView = (RelativeLayout) view.findViewById(R.id.layout_root_view);
			view.findViewById(R.id.tv_sure).setOnClickListener(this);
			view.findViewById(R.id.tv_cancle).setOnClickListener(this);

			mTvProName = (TextView) view.findViewById(R.id.tv_pro_name);
			mTvProCnt = (TextView) view.findViewById(R.id.tv_pro_cnt);
			mEtTcaiNum = (EditText) view.findViewById(R.id.et_tc_num);
			mEtTcaiReason = (EditText) view.findViewById(R.id.et_tc_reason);
			view.findViewById(R.id.iv_down_tag).setOnClickListener(this);

			mTvProName.setText(mProduct.getCodeName());
			mTvProCnt.setText(mProduct.getCnt() + mProduct.getUnits());

			initTuiReasonSp();
		} else if (mType == 3) {
			view = inflater.inflate(R.layout.product_gift_dialog, null, false);
			mRootView = (RelativeLayout) view.findViewById(R.id.layout_root_view);
			view.findViewById(R.id.tv_sure).setOnClickListener(this);
			view.findViewById(R.id.tv_cancle).setOnClickListener(this);
			mTvProName = (TextView) view.findViewById(R.id.tv_pro_name);
			mTvProCnt = (TextView) view.findViewById(R.id.tv_pro_cnt);
			mEtGiftNum = (EditText) view.findViewById(R.id.et_gift_num);
			mEtGiftReason = (EditText) view.findViewById(R.id.et_gift_reason);
			view.findViewById(R.id.iv_down_tag).setOnClickListener(this);

			mTvProName.setText(mProduct.getCodeName());
			mTvProCnt.setText(mProduct.getCnt() + mProduct.getUnits());

			initGiftReasonSp();
		} else if (mType == 4) {
			view = inflater.inflate(R.layout.product_gaidan_dialog, null, false);
			mRootView = (RelativeLayout) view.findViewById(R.id.layout_root_view);
			view.findViewById(R.id.tv_sure).setOnClickListener(this);
			view.findViewById(R.id.tv_cancle).setOnClickListener(this);

			mTvTableName = (TextView) view.findViewById(R.id.tv_table_name);
			mEtPersonNum = (EditText) view.findViewById(R.id.et_person_num);
			mEtWaiterCode = (EditText) view.findViewById(R.id.et_waiter_code);

			mTvTableName.setText(mTable.getDtablename());

		}

		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);

		return dialog;
	}

	private void initGiftReasonSp() {
		try {
			give_list = dbManager.selector(Canyin_Shop_Zsreson.class).findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<PopBean> lsGift = new ArrayList<PopBean>();
		for (Canyin_Shop_Zsreson item : give_list) {
			PopBean reason = new PopBean(item.getZsreson());
			lsGift.add(reason);
		}

		mSpGiftReason = new SpinerPopWindow(getActivity());// 楼层spinner
		mSpGiftReason.setSpinnerAdatper(Constans.SP_NORMAL, lsGift);
		mSpGiftReason.setItemListener(new IOnItemSelectListener() {// 点击监听
					@Override
					public void onItemClick(PopBean bean) {
						mEtGiftReason.setText(bean.getData());

					}
				});
	}

	private void initTuiReasonSp() {

		try {
			tuicai_list = dbManager.selector(Canyin_Shop_Tcreson.class)
					.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<PopBean> lsTcai = new ArrayList<PopBean>();
		for (Canyin_Shop_Tcreson item : tuicai_list) {
			PopBean reason = new PopBean(item.getReasonDescription());
			lsTcai.add(reason);
		}

		mSpTcaiReason = new SpinerPopWindow(getActivity());// 楼层spinner
		mSpTcaiReason.setSpinnerAdatper(Constans.SP_NORMAL, lsTcai);
		mSpTcaiReason.setItemListener(new IOnItemSelectListener() {// 点击监听
					@Override
					public void onItemClick(PopBean bean) {
						mEtTcaiReason.setText(bean.getData());
					}
				});
	}

	private void initTingSp() {
		List<Canyin_Shop_Floor> floors = null;
		try {
			floors = dbManager.selector(Canyin_Shop_Floor.class).findAll();// 查找所有楼层信息
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 初始数据
		mTvPopHall.setText(floors.get(0).getFloorname());// 开始没有数据
		String tempFloorCode = floors.get(0).getFloorcode();
		List<Canyin_Shop_Diningtable> tempTable = null;
		try {
			tempTable = dbManager
					.selector(Canyin_Shop_Diningtable.class)
					.where(WhereBuilder.b("floorcode", "=", tempFloorCode).and(
							"status", "=", 1).and("dTableName", "!=", mTable.getDtablename())).and("isRoom", "!=", 5).findAll();
		} catch (DbException e1) {
			e1.printStackTrace();
		}
		if (tempTable != null) {
			if (!tempTable.isEmpty()) {
				mTvPopTable.setText(tempTable.get(0).getDtablename());
				newTingBh = tempTable.get(0).getFloorcode();
				newTaiBh = tempTable.get(0).getCode();
				newLsh = tempTable.get(0).getLsh();
			}
			for (int i = 0; i < tempTable.size(); i++) {
				if (tempTable.get(i).getDtablename().equals(mTable.getDtablename())) {
					tempTable.remove(i);
				}
			}
			initTaiSp(tempTable);
		}

		mSpHall = new SpinerPopWindow(getActivity());// 楼层spinner
		mSpHall.setSpinnerAdatper(Constans.SP_Shop_Floor, floors);
		mSpHall.setItemListener(new IOnItemSelectListener() {// 点击监听
			@Override
			public void onItemClick(PopBean bean) {
				Canyin_Shop_Floor item = (Canyin_Shop_Floor) bean;
				mTvPopHall.setText(item.getFloorname());
				String FloorCode = item.getFloorcode();
				List<Canyin_Shop_Diningtable> tables = null;
				try {// 按条件查找台位信息
					tables = dbManager
							.selector(Canyin_Shop_Diningtable.class)
							.where(WhereBuilder.b("floorcode", "=", FloorCode)
									.and("status", "=", 1))
							.and("isRoom", "!=", 5).findAll();

				} catch (Exception e) {
					e.printStackTrace();
				}
				for (int i = 0; i < tables.size(); i++) {
					if (tables.get(i).getDtablename().equals(mTable.getDtablename())) {
						tables.remove(i);
					}
				}
				if (tables != null && !tables.isEmpty()) {
					Canyin_Shop_Diningtable table = tables.get(0);
					mTvPopTable.setText(table.getDtablename());
					newTingBh = table.getFloorcode();
					newTaiBh = table.getCode();
					newLsh = table.getLsh();
					
					initTaiSp(tables);
				} else {
					mTvPopTable.setText("");
					newTingBh = "";
					newTaiBh = "";
					newLsh = "";
					tables = new ArrayList<Canyin_Shop_Diningtable>();
					initTaiSp(tables);
				}
			}
		});
	}

	/**
	 * 加载台数据
	 * 
	 * @param tables
	 *            包含台位信息
	 */
	private void initTaiSp(List<Canyin_Shop_Diningtable> tables) {
		if (tables == null) {
			return;
		}

		mSpTable = new SpinerPopWindow(getActivity());
		mSpTable.setSpinnerAdatper(Constans.SP_Shop_Diningtable, tables);
		mSpTable.setItemListener(new IOnItemSelectListener() {

			@Override
			public void onItemClick(PopBean bean) {
				Canyin_Shop_Diningtable item = (Canyin_Shop_Diningtable) bean;
				mTvPopTable.setText(item.getDtablename());
				newTingBh = item.getFloorcode();
				newTaiBh = item.getCode();
				newLsh = item.getLsh();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_sure:
			if (mType == 1) {// 转菜
				if (TextUtils.isEmpty(newTingBh) || TextUtils.isEmpty(newTaiBh)
						|| TextUtils.isEmpty(newLsh)) {
					SysWorkTools.showToast(getActivity(), "请先选择目标台位");
					return;
				}

				doFecherTurnDish(mTable, newTingBh, newTaiBh, newLsh);
			} else if (mType == 2) {// 退菜
				try {
					float tNum = Float.parseFloat(mEtTcaiNum.getText()
							.toString().trim());
					if (tNum > mProduct.getCnt()) {
						SysWorkTools.showToast(getActivity(), "退菜数量不能大于菜品数量");
						return;
					}
					if (tNum <= 0) {
						SysWorkTools.showToast(getActivity(), "退菜数量不能等于零");
						return;
					}

					String tReason = mEtTcaiReason.getText().toString().trim();
					doFecherBackDish(tNum, tReason);

				} catch (Exception e) {
					e.printStackTrace();
					SysWorkTools.showToast(getActivity(), "输入的退菜数量不正确");
				}
			} else if (mType == 3) {// 赠菜
				try {
					float zNum = Float.parseFloat(mEtGiftNum.getText()
							.toString().trim());
					if (zNum > mProduct.getCnt()) {
						SysWorkTools.showToast(getActivity(), "赠菜数量不能大于菜品数量");
						return;
					}
					if (zNum <= 0) {
						SysWorkTools.showToast(getActivity(), "赠菜数量不能等于零");
						return;
					}

					String zReason = mEtGiftReason.getText().toString().trim();
					doFetchZengDish(zNum, zReason);

				} catch (Exception e) {
					e.printStackTrace();
					SysWorkTools.showToast(getActivity(), "输入的赠菜数量不正确");
				}
				
				SysWorkTools.closeKeyboard(mEtGiftNum);
				SysWorkTools.closeKeyboard(mEtGiftReason);
			} else if (mType == 4) {// 改单

				String personNum = mEtPersonNum.getText().toString().trim();
				String waiterCode = mEtWaiterCode.getText().toString().trim();
				
				if(TextUtils.isEmpty(personNum) && TextUtils.isEmpty(waiterCode)){
					SysWorkTools.showToast(getActivity(), "就餐人数或服务员编号不能为空");
					return;
				}
				
				try {
					if (Integer.parseInt(personNum) < 1) {
						SysWorkTools.showToast(getActivity(), "就餐人数不能小于1");
						return;
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				if (!TextUtils.isEmpty(waiterCode) && waiterCode.length() < 2) {
					SysWorkTools.showToast(getActivity(), "服务员编号不能少于两位");
					return;
				} 
				doFetchChangeDish(personNum, waiterCode);
				SysWorkTools.closeKeyboard(mEtPersonNum);
				SysWorkTools.closeKeyboard(mEtWaiterCode);
			}
			break;
		case R.id.tv_cancle:
			SysWorkTools.closeKeyboard(view);
			dismiss();
			
			break;
		case R.id.iv_down_tag:
			if (mType == 3) {// 赠菜原因
				if (mSpGiftReason != null) {
					mSpGiftReason.setWidth(mEtGiftReason.getWidth());
					mSpGiftReason.showAsDropDown(mEtGiftReason);
				}
			} else if (mType == 2) {// 退菜原因
				if (mSpTcaiReason != null) {
					mSpTcaiReason.setWidth(mEtTcaiReason.getWidth());
					mSpTcaiReason.showAsDropDown(mEtTcaiReason);
				}
			}
			break;
		case R.id.tv_hall:
			if (mSpHall != null) {
				mSpHall.setWidth(mTvPopHall.getWidth());
				mSpHall.showAsDropDown(mTvPopHall);
			}
			break;
		case R.id.tv_table:
			if (mSpTable != null) {
				mSpTable.setWidth(mTvPopTable.getWidth());
				mSpTable.showAsDropDown(mTvPopTable);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 改单接口
	 * 
	 * @param personNum
	 * @param waiterCode
	 */
	private void doFetchChangeDish(String personNum, String waiterCode) {
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fetchChangeOrder(new FetchListener() {

				@Override
				public void onPreFetch() {
					ProgressView.getInstance(getActivity()).showProgress(mRootView);
				}

				@Override
				public void onPostFetch(int status, Object... result) {
					ProgressView.getInstance(getActivity()).hideProgress(mRootView);
					if (status == FetchListener.STATUS_OK) {
						if (result != null ) {
						
							//HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_CHANGE_WORKNAME, null);
						}
						HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_GAIDAN_SUCC, "");
						SysWorkTools.showToast(getActivity(), "改单成功");
						dismiss();
					} else {
						String msg = (String) result[0];
						SysWorkTools.showToast(getActivity(), msg);
						dismiss();
					}
					
					toDismiss();

				}
			}, mTable.getLsh(), String.valueOf(mTable.getPersoncnt()),personNum,waiterCode);
		}else {
			SysWorkTools.showToast(getActivity(), "请检查网络");
		}
		
	}

	private void toDismiss() {
		dismiss();
	}
	/**
	 * 转菜
	 * @param oldTable
	 * @param newtingbh
	 * @param newtaibh
	 * @param newlsh
	 */
	protected void doFecherTurnDish(Canyin_Shop_Diningtable oldTable,
			String newtingbh, String newtaibh, String newlsh) {
		JSONArray jaArray = new JSONArray();
		JSONObject jObject = new JSONObject();
		jObject.put("id", mProduct.getpId());
		jaArray.add(jObject);
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fecthTurnOrderDish(
					oldTable.getFloorcode(), oldTable.getCode(), oldTable.getLsh(),
					newtingbh, newtaibh, newlsh, jaArray.toJSONString(),
					new FetchListener() {

						@Override
						public void onPreFetch() {
							ProgressView.getInstance(getActivity()).showProgress(mRootView);
						}

						@Override
						public void onPostFetch(int status, Object... result) {
							ProgressView.getInstance(getActivity()).hideProgress(mRootView);
							if (status == FetchListener.STATUS_OK) {
								HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_ZHUANCAI_SUCC, "");
								SysWorkTools.showToast(getActivity(), "转菜成功");
								toDismiss();
							}else {
								String msg = (String) result[0];
								SysWorkTools.showToast(getActivity(), msg);
								toDismiss();
							}

						}
					});
		}else {
			SysWorkTools.showToast(getActivity(), "请检查网络");
		}
		

	}
	/**
	 * 退菜
	 * @param tNum
	 * @param tReason
	 */
	private void doFecherBackDish(float tNum, String tReason) {
		SysWorkTools.closeKeyboard(mEtTcaiReason);

		TuiCai tuiCai = new TuiCai();
		tuiCai.setId(mProduct.getpId());
		tuiCai.setCnt(String.valueOf(mProduct.getCnt()));
		tuiCai.setTccnt(String.valueOf(tNum));
		tuiCai.setTcycode(PrefsConfig.workerNum);
		tuiCai.setTcyname(PrefsConfig.workerName);

		String tCode = "";
		for (Canyin_Shop_Tcreson item : tuicai_list) {
			if (item.getReasonDescription().equals(tReason)) {
				tCode = item.getBh();
			}
		}

		tuiCai.setBackcode(tCode);
		tuiCai.setBackname(tReason);
		JSONArray jaArray = new JSONArray();
		jaArray.add(tuiCai);
		String data = jaArray.toJSONString();
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fecthReturnDishes(mProduct.getTingBh(),
					mProduct.getTaiBh(), mProduct.getLsh(), data,
					new FetchListener() {

						@Override
						public void onPreFetch() {
							ProgressView.getInstance(getActivity()).showProgress(mRootView);
						}

						@Override
						public void onPostFetch(int status, Object... result) {
							ProgressView.getInstance(getActivity()).hideProgress(mRootView);
							if (status == FetchListener.STATUS_OK) {
								// TuiFoodResult tuiFood = (TuiFoodResult)
								// result[0];
								HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_TUICAI_SUCC, "");
								SysWorkTools.showToast(getActivity(), "退菜成功");
							} else {
								String msg = (String) result[0];
								SysWorkTools.showToast(getActivity(), msg);
							}
							
							toDismiss();
						}
					});
		}else {
			SysWorkTools.showToast(getActivity(), "请检查网络");

		}
		
	}
	
	/**
	 * 赠菜
	 * @param zNum
	 * @param zReason
	 */
	private void doFetchZengDish(float zNum, String zReason) {
		String netid = "";
		GiveDishDataItemBean giveBean = new GiveDishDataItemBean();
		giveBean.setId(mProduct.getpId());
		giveBean.setCnt(mProduct.getCnt());
		giveBean.setZscnt(String.valueOf(zNum));
		giveBean.setZscode(mProduct.getStatus());
		giveBean.setZsname(zReason);
		giveBean.setZsycode(PrefsConfig.workerNum);
		giveBean.setZsyname(PrefsConfig.workerName);
		giveBean.setNetid(netid);
		JSONArray jaArray = new JSONArray();
		jaArray.add(giveBean);
		String data = jaArray.toJSONString();
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fetchGiveDish(mProduct.getTingBh(),
					mProduct.getTaiBh(), mProduct.getLsh(), data,
					new FetchListener() {

						@Override
						public void onPreFetch() {
							ProgressView.getInstance(getActivity()).showProgress(mRootView);
						}

						@Override
						public void onPostFetch(int status, Object... result) {
							ProgressView.getInstance(getActivity()).hideProgress(mRootView);
							if (status == FetchListener.STATUS_OK) {
								SysWorkTools.showToast(getActivity(), "赠菜成功");
								HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_ZENGCAI_SUCC, "");
							} else {
								String msg = (String) result[0];
								SysWorkTools.showToast(getActivity(), msg);
							}
							
							toDismiss();
							SysWorkTools.closeKeyboard(getActivity());
						}
					});
		}else {
			SysWorkTools.showToast(getActivity(), "请检查网络");

		}
		
	}

}