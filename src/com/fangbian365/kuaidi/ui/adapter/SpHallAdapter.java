package com.fangbian365.kuaidi.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Floor;
import com.fangbian365.kuaidi.ui.adapter.base.BaseSingleTypeAdapter;

public class SpHallAdapter extends BaseSingleTypeAdapter<Canyin_Shop_Floor> {

	public SpHallAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.spiner_item_layout, null);
			holder = new ViewHolder();
			holder.mHallname = (TextView) convertView.findViewById(R.id.tv_name);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Canyin_Shop_Floor item = getItem(position);
		if (null != item) {
			holder.mHallname.setText(item.getFloorname());
		}

		return convertView;
		
	}

	
	@Override
	public Canyin_Shop_Floor getItem(int position) {
		if (mItems == null)
			return null;
		int p = position % mItems.size();
		return super.getItem(p);
	}
	
	static class ViewHolder {
		public TextView mHallname;
	}
}
