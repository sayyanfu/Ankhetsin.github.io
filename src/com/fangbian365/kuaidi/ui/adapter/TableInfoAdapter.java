package com.fangbian365.kuaidi.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Diningtable;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.ui.adapter.base.BaseSingleTypeAdapter;

public class TableInfoAdapter extends BaseSingleTypeAdapter<Canyin_Shop_Diningtable> {
	
	private Resources mResource;
	
	public TableInfoAdapter(Context context) {
		super(context);
		mResource = mContext.getResources();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.table_item, null);
			holder = new ViewHolder();
			holder.mTablecode = (TextView) convertView.findViewById(R.id.tv_table_code);
			holder.mTablename = (TextView) convertView.findViewById(R.id.tv_table_name);
			holder.mSeat = (TextView) convertView.findViewById(R.id.tv_table_person_count);
			holder.mIvStatusBg = (RelativeLayout) convertView.findViewById(R.id.layout_table_bg);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Canyin_Shop_Diningtable item = getItem(position);
		if (item != null) {
			holder.mTablename.setText(item.getDtablename());
			switch (item.getStatus()) {
			case Constans.TABLE_IDLE://空闲
				holder.mTablecode.setText(item.getCode());
				holder.mTablename.setText(item.getDtablename());
				
				if(item.getIsroom() == 5) {
					holder.mSeat.setVisibility(View.INVISIBLE);
				} else {
					holder.mSeat.setText("0/" +item.getMaxseat());
					holder.mSeat.setVisibility(View.VISIBLE);
				}
				
				holder.mTablecode.setTextColor(mResource.getColor(R.color.white));
				holder.mTablename.setTextColor(mResource.getColor(R.color.white));
				holder.mSeat.setTextColor(mResource.getColor(R.color.white));
				
				if(item.isChoosed()) {
					holder.mIvStatusBg.setBackgroundResource(R.drawable.bg_table_idle_press);
				} else {
					holder.mIvStatusBg.setBackgroundResource(R.drawable.selector_bg_table_idle);
				}
				
				break;
			case Constans.TABLE_INUSE://已开台
				holder.mTablecode.setText(item.getCode());
				holder.mTablename.setText(item.getDtablename());
				
				holder.mSeat.setText(item.getPersoncnt() + "/" +item.getMaxseat());
				holder.mSeat.setVisibility(View.VISIBLE);
				
				holder.mTablecode.setTextColor(mResource.getColor(R.color.white));
				holder.mTablename.setTextColor(mResource.getColor(R.color.white));
				holder.mSeat.setTextColor(mResource.getColor(R.color.white));
				
				if(item.isChoosed()) {
					holder.mIvStatusBg.setBackgroundResource(R.drawable.bg_table_inuse_press);
				} else {
					holder.mIvStatusBg.setBackgroundResource(R.drawable.selector_bg_table_inuse);
				}
				break;
			case Constans.TABLE_CLEAN://待清
				holder.mTablecode.setText(item.getCode());
				holder.mTablename.setText(item.getDtablename());
				
				holder.mSeat.setText("0" + "/" +item.getMaxseat());
				holder.mSeat.setVisibility(View.VISIBLE);
				
				holder.mTablecode.setTextColor(mResource.getColor(R.color.table_disable));
				holder.mTablename.setTextColor(mResource.getColor(R.color.table_disable));
				holder.mSeat.setTextColor(mResource.getColor(R.color.table_disable));
				
				if(item.isChoosed()) {
					holder.mIvStatusBg.setBackgroundResource(R.drawable.bg_table_clean_press);
				} else {
					holder.mIvStatusBg.setBackgroundResource(R.drawable.selector_bg_table_for_clean);
				}
				break;
			case Constans.TABLE_DISABLE://禁用
				holder.mTablecode.setText(item.getCode());
				holder.mTablename.setText(item.getDtablename());
				
				holder.mSeat.setText("0" + "/" +item.getMaxseat());
				holder.mSeat.setVisibility(View.VISIBLE);
				
				holder.mTablecode.setTextColor(mResource.getColor(R.color.white));
				holder.mTablename.setTextColor(mResource.getColor(R.color.white));
				holder.mSeat.setTextColor(mResource.getColor(R.color.white));
				
				if(item.isChoosed()) {
					holder.mIvStatusBg.setBackgroundResource(R.drawable.bg_table_disable_press);
				} else {
					holder.mIvStatusBg.setBackgroundResource(R.drawable.selector_bg_table_disable);
				}
				break;
			default:
				break;
			}
			
		}

		return convertView;
	}

	
	@Override
	public Canyin_Shop_Diningtable getItem(int position) {
		if (mItems == null)
			return null;
		int p = position % mItems.size();
		return super.getItem(p);
	}
	
	static class ViewHolder {
		public TextView mTablecode;
		public TextView mTablename;
		public TextView mSeat;
		public RelativeLayout mIvStatusBg;// iv_bg空闲、占用、禁用等状态对应不同的背景
		
	}
	
}
