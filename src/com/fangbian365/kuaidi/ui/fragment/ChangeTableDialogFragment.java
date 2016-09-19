package com.fangbian365.kuaidi.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSONArray;
import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.CHBApp;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Diningtable;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Floor;
import com.fangbian365.kuaidi.base.bean.HuanTaiBean;
import com.fangbian365.kuaidi.base.bean.HuanTaiResult;
import com.fangbian365.kuaidi.base.bean.PopBean;
import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.mod.asynctask.http.FetchListener;
import com.fangbian365.kuaidi.mod.manager.DataFetchManager;
import com.fangbian365.kuaidi.ui.uisupport.ProgressView;
import com.fangbian365.kuaidi.ui.uisupport.SpinerPopWindow;
import com.fangbian365.kuaidi.ui.uisupport.SpinerPopWindow.IOnItemSelectListener;
import com.umeng.analytics.MobclickAgent;

public class ChangeTableDialogFragment extends DialogFragment implements OnClickListener{
	
	private static final String TAG = "ChangeTableDialogFragment";

	private RelativeLayout mRootView;
	private TextView mTvOldTable;//tv_old_table
	private TextView mTvPopHall;//tv_pop_hall
	private TextView mTvPopTable;//tv_pop_table
	private DbManager dbManager;
	private SpinerPopWindow mSpHall = null;
	private SpinerPopWindow mSpTable = null;
	private Canyin_Shop_Diningtable mOldTable;
	private String newTingBh = "";
	private String newTaiBh = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mOldTable = (Canyin_Shop_Diningtable) bundle.getSerializable(Constans.PARAM_TABLE_OLD);
		}
		dbManager = CHBApp.getInstance().getDbManager();//初始化数据库
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
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.change_table_dialog, null, false);
		mRootView = (RelativeLayout) view.findViewById(R.id.layout_root_view);
		view.findViewById(R.id.tv_sure).setOnClickListener(this);
		view.findViewById(R.id.tv_cancle).setOnClickListener(this);
		mTvOldTable = (TextView) view.findViewById(R.id.tv_old_table);
		mTvPopHall = (TextView) view.findViewById(R.id.tv_pop_hall);
		mTvPopTable = (TextView) view.findViewById(R.id.tv_pop_table);
		mTvPopHall.setOnClickListener(this);
		mTvPopTable.setOnClickListener(this);
		mTvOldTable.setText(mOldTable.getDtablename());
		initTingSp();
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		return dialog;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_pop_hall:
			if(mSpHall != null) {
				mSpHall.setWidth(mTvPopHall.getWidth());
				mSpHall.showAsDropDown(mTvPopHall);
			}
			break;
		case R.id.tv_pop_table:
			if(mSpTable != null) {
				mSpTable.setWidth(mTvPopTable.getWidth());
				mSpTable.showAsDropDown(mTvPopTable);
			}
			break;
		case R.id.tv_sure:
			if(TextUtils.isEmpty(newTingBh) || TextUtils.isEmpty(newTaiBh)) {
				SysWorkTools.showToast(getActivity(), "请先选择目标台位");
				return;
			}
			HuanTaiBean bean = new HuanTaiBean(mOldTable, newTingBh, newTaiBh);
			changeTable(bean);
			break;
		case R.id.tv_cancle:
			dismiss();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 加载厅数据
	 */
	private void initTingSp() {
		List<Canyin_Shop_Floor> floors = null;
		try {
			floors = dbManager.selector(Canyin_Shop_Floor.class).findAll();//查找所有楼层信息
		} catch (Exception e) {
			e.printStackTrace();
		}
		//初始数据
		mTvPopHall.setText(floors.get(0).getFloorname());//开始没有数据
		String tempFloorCode = floors.get(0).getFloorcode();
		List<Canyin_Shop_Diningtable>  tempTable = null;
		try {
			tempTable = dbManager
					.selector(Canyin_Shop_Diningtable.class)
					.where(WhereBuilder.b("floorcode", "=", tempFloorCode).and(
							"status", "=", 0)).and("isRoom", "!=", 5).findAll();
		} catch (DbException e1) {
			e1.printStackTrace();
		}
		if (tempTable != null) {
			if (!tempTable.isEmpty()) {
				mTvPopTable.setText(tempTable.get(0).getDtablename());
				newTingBh = tempTable.get(0).getFloorcode();
				newTaiBh = tempTable.get(0).getCode();
			}
			initTaiSp(tempTable);
		}
		
		mSpHall = new SpinerPopWindow(getActivity());//楼层spinner
		mSpHall.setSpinnerAdatper(Constans.SP_Shop_Floor, floors);
		mSpHall.setItemListener(new IOnItemSelectListener() {//点击监听
			@Override
			public void onItemClick(PopBean bean) {
				Canyin_Shop_Floor item = (Canyin_Shop_Floor)bean;
				mTvPopHall.setText(item.getFloorname());
				String FloorCode = item.getFloorcode();
				List<Canyin_Shop_Diningtable> tables = null;
				try {//按条件查找台位信息
					tables = dbManager
							.selector(Canyin_Shop_Diningtable.class)
							.where(WhereBuilder.b("floorcode", "=", FloorCode).and(
									"status", "=", 0)).and("isRoom", "!=", 5).findAll();
					
					//.and("code", "!=", mOldTable.getCode())

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (tables != null && !tables.isEmpty()) {
					Canyin_Shop_Diningtable table = tables.get(0);
					mTvPopTable.setText(table.getDtablename());
					newTingBh = table.getFloorcode();
					newTaiBh = table.getCode();
					initTaiSp(tables);
				}else {
					mTvPopTable.setText("");
					newTingBh = "";
					newTaiBh = "";
					tables = new ArrayList<Canyin_Shop_Diningtable>();
					initTaiSp(tables);
				}
			}
		});
	}
	
	/**
	 * 加载台数据
	 * @param tables 包含台位信息
	 */
	private void initTaiSp(List<Canyin_Shop_Diningtable> tables){
		if(tables == null) {
			return;
		}
		mSpTable = new SpinerPopWindow(getActivity());
		mSpTable.setSpinnerAdatper(Constans.SP_Shop_Diningtable, tables);
		mSpTable.setItemListener(new IOnItemSelectListener() {
			
			@Override
			public void onItemClick(PopBean bean) {
				Canyin_Shop_Diningtable item = (Canyin_Shop_Diningtable)bean;
				mTvPopTable.setText(item.getDtablename());
				newTingBh = item.getFloorcode();
				newTaiBh = item.getCode();
			}
		});
	}
	
	/**
	 * 变更位置
	 */
	private void changeTable(HuanTaiBean huantai) {
		JSONArray arr = new JSONArray();
		arr.add(huantai);
		String jsonStr = arr.toJSONString();
		FrameLog.d(TAG, jsonStr);
		if (SysWorkTools.isNetAvailable()) {
			DataFetchManager.getInstance().fetchChangeTablePosition(jsonStr,
					new FetchListener() {

						@Override
						public void onPreFetch() {
							ProgressView.getInstance(getActivity()).showProgress(mRootView);
						}

						public void onPostFetch(int status, Object... result) {
							ProgressView.getInstance(getActivity()).hideProgress(mRootView);
							if (status == FetchListener.STATUS_OK) {

								HuanTaiResult huantaiResult = (HuanTaiResult) result[0];

//								PrinterDeviceManager.getInstance().printeChangeTable(huantaiResult);

								SysWorkTools.showToast(getActivity(), "换台成功");
//								((MainActivity) getActivity()).afterChangeTable();

							} else {
								SysWorkTools.showToast(getActivity(), "换台失败");
							}
							ChangeTableDialogFragment.this.dismiss();
						}
					});
		}else {
			SysWorkTools.showToast(getActivity(), "请检查网络");
		}
		
	}
	
}
