package com.fangbian365.kuaidi.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Product_Type;
import com.fangbian365.kuaidi.ui.adapter.base.BaseSingleTypeAdapter;

public class ProTypeAdapter extends BaseSingleTypeAdapter<Canyin_Shop_Product_Type> {

	public ProTypeAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
	   	 
	     if (convertView == null) {
	    	 convertView = View.inflate(mContext, R.layout.spiner_item_layout, null);
	         viewHolder = new ViewHolder();
	         viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_name);
	         convertView.setTag(viewHolder);
	     } else {
	         viewHolder = (ViewHolder) convertView.getTag();
	     }
	     
	     Canyin_Shop_Product_Type item =  getItem(position);
		 viewHolder.mTextView.setText(item.getTypename());

	     return convertView;
	}

	
	@Override
	public Canyin_Shop_Product_Type getItem(int position) {
		if (mItems == null)
			return null;
		int p = position % mItems.size();
		return super.getItem(p);
	}
	
	public static class ViewHolder
	{
	    public TextView mTextView;
   }
}
