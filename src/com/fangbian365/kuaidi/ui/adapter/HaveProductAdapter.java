package com.fangbian365.kuaidi.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.HaveProductBean;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.ui.adapter.base.BaseSingleTypeAdapter;
/**
 * 已点菜明细适配器
 * @author houjianjiang
 *
 */
public class HaveProductAdapter extends BaseSingleTypeAdapter<HaveProductBean> {

	public HaveProductAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.haveproduct_listvitem, null);
			viewHolder = new ViewHolder();

			viewHolder.tv_pinyin_tag = (TextView) convertView.findViewById(R.id.tv_pinyin_tag);
			viewHolder.tv_pro_name = (TextView) convertView.findViewById(R.id.tv_pro_name);
			viewHolder.tv_status = (ImageView) convertView.findViewById(R.id.tv_status);
			viewHolder.tv_pro_price = (TextView) convertView.findViewById(R.id.tv_pro_price);
			viewHolder.tv_pro_remark = (TextView) convertView.findViewById(R.id.tv_pro_remark);
			viewHolder.tv_pro_num = (TextView) convertView.findViewById(R.id.tv_pro_num);
			viewHolder.tv_pro_wait_status = (TextView) convertView.findViewById(R.id.tv_pro_wait_status);
			viewHolder.devide_line = (View) convertView.findViewById(R.id.huacai_line);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		HaveProductBean item = getItem(position);//获取一条数据
		if (item != null) {
			String pinyin = "";
			if(!TextUtils.isEmpty(item.getPym())) {
				pinyin = item.getPym().substring(0, 1);
			}  
			viewHolder.tv_pinyin_tag.setBackgroundResource(SysWorkTools.getPinyinBg(pinyin));
			viewHolder.tv_pinyin_tag.setText(pinyin);
			viewHolder.tv_pro_name.setText(item.getCodeName());
			viewHolder.tv_pro_price.setText(item.getPrice() + "/" + item.getUnits());
			if (TextUtils.isEmpty(item.getMarkTaste())) {
				viewHolder.tv_pro_remark.setVisibility(View.GONE);
			}else {
				viewHolder.tv_pro_remark.setVisibility(View.VISIBLE);
				viewHolder.tv_pro_remark.setText(item.getMarkTaste());
			}
			viewHolder.tv_pro_num.setText(item.getCnt() + "份");
			viewHolder.tv_pro_wait_status.setText(item.getWaitStatus());//起叫状态
			//划菜
			if (null != item.getMarkStatus() && (item.getMarkStatus().equals("3") || item.getMarkStatus().equals("已上"))) {
				viewHolder.devide_line.setVisibility(View.VISIBLE);
			} else {
				viewHolder.devide_line.setVisibility(View.GONE);
			}
			if(item.getStatus() == 2) {
				viewHolder.tv_status.setVisibility(View.VISIBLE);
				viewHolder.tv_status.setImageResource(R.drawable.give_bg);
			} else if(item.getStatus() == 3) {
				viewHolder.tv_status.setVisibility(View.VISIBLE);
				viewHolder.tv_status.setImageResource(R.drawable.tui_bg);
			} else {
				viewHolder.tv_status.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

	@Override
	public HaveProductBean getItem(int position) {
		if (mItems == null)
			return null;
		int p = position % mItems.size();
		return super.getItem(p);
	}
	
	@Override
	public boolean getSwipEnableByPosition(int position) {
		// TODO Auto-generated method stub
		return super.getSwipEnableByPosition(position);
	}

	/**
	 * ViewHolder 控件初始化
	 */
	public static class ViewHolder {
		public TextView tv_pinyin_tag;;
		public TextView tv_pro_name;
		public ImageView tv_status;
		public TextView tv_pro_price;
		public TextView tv_pro_remark;
		public TextView tv_pro_num;
		public TextView tv_pro_wait_status;
		private View devide_line;
	}


}