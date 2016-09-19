package com.fangbian365.kuaidi.ui.adapter;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Product;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.ui.adapter.base.BaseSingleTypeAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 拼音 
 */
public class PinyinAdapter extends BaseSingleTypeAdapter<Canyin_Shop_Product> {
	public PinyinAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			BaseDishViewHolder baseDishViewHolder = null;
			if (convertView == null) {
				convertView = View.inflate(mContext,R.layout.pinyin_listvitem, null);
				baseDishViewHolder = new BaseDishViewHolder();
				
				baseDishViewHolder.pinyin_tv = (TextView) convertView.findViewById(R.id.tv_pinyin_tag);
				baseDishViewHolder.dishName_tv = (TextView) convertView.findViewById(R.id.tv_pro_name);
				baseDishViewHolder.dishPrice_tv = (TextView) convertView.findViewById(R.id.tv_pro_price);
				
				convertView.setTag(baseDishViewHolder);
			}else {
				baseDishViewHolder = (BaseDishViewHolder) convertView.getTag();
			}
			
			Canyin_Shop_Product data = getItem(position);
			String pin = (String)((TextUtils.isEmpty(data.getPinyin())) ?  "" : data.getPinyin().subSequence(0, 1));
			baseDishViewHolder.pinyin_tv.setText(pin);
			baseDishViewHolder.pinyin_tv.setBackgroundResource(SysWorkTools.getPinyinBg(pin)); //设置拼音的背景
			baseDishViewHolder.dishName_tv.setText(data.getProductname());
			baseDishViewHolder.dishPrice_tv.setText("￥" + data.getCurrentprice() + "/" + data.getUnits());
		return convertView;
	}
	
	static class BaseDishViewHolder{
		TextView pinyin_tv; //拼音
		TextView dishName_tv; //菜名
		TextView dishPrice_tv; //菜价
	}
	
}
