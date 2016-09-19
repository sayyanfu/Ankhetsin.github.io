package com.fangbian365.kuaidi.ui.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.MsgBean;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.ui.adapter.base.BaseSingleTypeAdapter;

public class MsgAdapter extends BaseSingleTypeAdapter<MsgBean> {
	
	private SimpleDateFormat df;

	public MsgAdapter(Context context) {
		super(context);
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_message, null);
			viewHolder = new ViewHolder();
			viewHolder.mTvTime = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_msg_title);
			viewHolder.mTvTotleMoney = (TextView) convertView.findViewById(R.id.tv_total_money);
			viewHolder.mIvDetail = (ImageView) convertView.findViewById(R.id.iv_detail);
			viewHolder.mTvPos = (TextView) convertView.findViewById(R.id.tv_msg_pos);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		MsgBean item = getItem(position);
		if(item != null) {
			Date date = null;
			try {
				date = df.parse(item.getStartTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(date != null) {
				viewHolder.mTvTime.setText(Constans.D_FORMAT_HM.format(date));	
			}
			
			String s = item.getTingName();
			String t = item.getTaiName();
			
			if (null == item.getTingName()) {
				s = "";
			}
			if (null == item.getTaiName()) {
				t = "";
			}
			if (s.equals("") || t.equals("")) {
				viewHolder.mTvTitle.setText(item.getPos() + "点餐");
				viewHolder.mTvPos.setVisibility(View.GONE);
			} else {
				viewHolder.mTvTitle.setText(s + t);
				viewHolder.mTvPos.setVisibility(View.VISIBLE);
				viewHolder.mTvPos.setText(item.getPos());
			}
			
			viewHolder.mTvTotleMoney.setText("￥" + String.valueOf(item.getPriceTotal()));
			viewHolder.mIvDetail.setTag(item);
		}
		

		return convertView;
	}

	@Override
	public MsgBean getItem(int position) {
		if (mItems == null)
			return null;
		int p = position % mItems.size();
		return super.getItem(p);
	}

	public static class ViewHolder {
		public TextView mTvTime;
		public TextView mTvTitle;
		public TextView mTvTotleMoney;
		public ImageView mIvDetail;
		public TextView mTvPos;
	}

}
