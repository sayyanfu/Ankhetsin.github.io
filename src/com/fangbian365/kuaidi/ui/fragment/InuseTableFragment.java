package com.fangbian365.kuaidi.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Diningtable;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.ui.activity.MainActivity;
import com.fangbian365.kuaidi.ui.adapter.TableInfoAdapter;
import com.fangbian365.kuaidi.ui.receiver.HomeBroadcastReceiver;
import com.fangbian365.kuaidi.ui.receiver.IDataTransfer;
import com.umeng.analytics.MobclickAgent;

public class InuseTableFragment extends Fragment implements OnItemClickListener, IDataTransfer {

	private GridView mGvAllTable;
	private TableInfoAdapter mTableAdapter;
	
	private int iFrom = -1;
	private List<Canyin_Shop_Diningtable> tList;
	private final String TAG =InuseTableFragment.class.getSimpleName();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle != null) {
			iFrom = bundle.getInt(Constans.KEY_TABLE_FROM);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup contentView = (ViewGroup) inflater.inflate(R.layout.fragment_table_inuse, null);
		mGvAllTable = (GridView) contentView.findViewById(R.id.gv_inuse_table);
		mGvAllTable.setOnItemClickListener(this);
		mTableAdapter = new TableInfoAdapter(getActivity());
		mGvAllTable.setAdapter(mTableAdapter);
		
		return contentView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Canyin_Shop_Diningtable table = (Canyin_Shop_Diningtable) parent.getItemAtPosition(position);
		
		if(iFrom == -1) {
			table.setChoosed(true);
			mTableAdapter.notifyDataSetChanged();
			((MainActivity) getActivity()).showPopupMenu(table);
		} else {
			for (Canyin_Shop_Diningtable item : tList) {
				item.setChoosed(false);
			}
			table.setChoosed(true);
			mTableAdapter.notifyDataSetChanged();
			HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_CHOOSE_TABLE, table);
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		HomeBroadcastReceiver.unregisterIDataTransfer(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onDataTransfer(int type, Object... data) {
		if(type == Constans.TYPE_TABLE_CHANGED) {
			tList = (List<Canyin_Shop_Diningtable>) data[0];
			String curHall = (String) data[1];
			
			boolean bMsgFrom = (Boolean) data[2];
			
			if(!bMsgFrom && iFrom == -1) {
				refreshTable(tList, curHall);
			}
			
			if(bMsgFrom && iFrom != -1) {
				refreshTable(tList, curHall);
			}
		} else if(type == Constans.TYPE_TABLE_SEARCH) {
			int curPosition = (Integer) data[0];
			List<Canyin_Shop_Diningtable> searchList = (List<Canyin_Shop_Diningtable>) data[1];
			if(curPosition == 2) {
				mTableAdapter.setItems(searchList);
				mTableAdapter.notifyDataSetChanged();
			}
		}
	}
	
	private void refreshTable(List<Canyin_Shop_Diningtable> tList, String curHall) {
		
		List<Canyin_Shop_Diningtable> tempList = new ArrayList<Canyin_Shop_Diningtable>();
		for (Canyin_Shop_Diningtable item : tList) {
			if(item.getStatus() == 1 && (curHall.equals("全部") ? true : item.getFloorcode().equals(curHall))) {
				tempList.add(item);
			}
		}
		
		mTableAdapter.setItems(tempList);
		mTableAdapter.notifyDataSetChanged();
	}
}
