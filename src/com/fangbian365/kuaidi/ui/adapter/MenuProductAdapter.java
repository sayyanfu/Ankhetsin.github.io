package com.fangbian365.kuaidi.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.HaveProductBean;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.ui.adapter.base.BaseSingleTypeAdapter;
import com.fangbian365.kuaidi.ui.receiver.HomeBroadcastReceiver;

public class MenuProductAdapter extends BaseSingleTypeAdapter<HaveProductBean> {
	private static final String TAG = "MenuProductAdapter";
	
	
	public MenuProductAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = LayoutInflater.from(mContext).inflate(R.layout.item_product_add_layout, null);
			holder.productPinyin = (TextView) contentView.findViewById(R.id.tv_pinyin_tag);
			holder.productName = (TextView) contentView.findViewById(R.id.product_name);
			holder.productPrince = (TextView) contentView.findViewById(R.id.product_prince);
			holder.productLow = (ImageView) contentView.findViewById(R.id.have_prod_subbt);
			holder.productAdd = (ImageView) contentView.findViewById(R.id.have_prod_addbt);
			holder.productSum = (TextView) contentView.findViewById(R.id.have_prod_tv);
//			holder.productStatus = (TextView) contentView.findViewById(R.id.product_status);
			holder.productAdd.setOnClickListener(mAddClickListener);
			holder.productLow.setOnClickListener(mSubClickListener);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		HaveProductBean product = getItem(position);

		if (product != null) {
			String pin = (String)((TextUtils.isEmpty(product.getPinyin())) ?  "" : product.getPinyin().subSequence(0, 1));
			holder.productPinyin.setText(pin);
			holder.productPinyin.setBackgroundResource(SysWorkTools.getPinyinBg(pin)); //设置拼音的背景
			holder.productName.setText(product.getCodeName());
			holder.productPrince.setText("¥" + Constans.DF.format(product.getPrice()) + "" + "/"
					+ product.getUnits());
			if (product.getCnt() > 0) {
				holder.productLow.setVisibility(View.VISIBLE);
				holder.productSum.setVisibility(View.VISIBLE);
			}else {
				holder.productLow.setVisibility(View.GONE);
				holder.productSum.setVisibility(View.GONE);
			}
			holder.productLow.setTag(position);
			holder.productSum.setText(String.valueOf(product.getCnt()));
			holder.productAdd.setTag(position);
//			holder.productStatus.setText(product.getWaitStatus());
		}
		return contentView;
	}
	
	@Override
	public HaveProductBean getItem(int position) {
		if (mItems == null)
			return null;
		int p = position % mItems.size();
		return super.getItem(p);
	}

	/**
	 * 菜品数量“-”点击监听
	 */
	private OnClickListener mSubClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			HaveProductBean product = mItems.get(position);
			if (product.getOrderState() == 0) {// 未下单可以操作菜品数量
				float count = product.getCnt();
				if (count >= 1) {
					count--;
				}
				product.setCnt(count);
				notifyDataSetChanged();// 刷新列表
				HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_MENU_PRO_ADD_SUB, product);
			}
		}
	};

	/**
	 * 菜品数量增加“+”点击监听
	 */
	private OnClickListener mAddClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			HaveProductBean product = mItems.get(position);
			if (product.getOrderState() == 0) {// 未下单
				float count = product.getCnt();
				if (count < 100) {
					count++;
				}
				product.setCnt(count);// 修改数量
				notifyDataSetChanged();// 刷新列表
				HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_MENU_PRO_ADD_SUB, product);
			}
		}
	};
	
	
	class ViewHolder {
		TextView productPinyin;//拼音首字母
		TextView productName; //菜品名称
		TextView productPrince; //菜品价格
		TextView productSum; //菜品数量
		ImageView productLow; //减号按钮
		ImageView productAdd; //加号按钮
//		TextView productStatus; //菜品状态

	}

}