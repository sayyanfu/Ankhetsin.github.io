package com.fangbian365.kuaidi.ui.adapter;

import java.text.DecimalFormat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.MsgProBean;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.constans.Constans;
import com.fangbian365.kuaidi.ui.activity.MainActivity;
import com.fangbian365.kuaidi.ui.adapter.base.BaseSingleTypeAdapter;
import com.fangbian365.kuaidi.ui.fragment.InputProNumDialogFragment;
import com.fangbian365.kuaidi.ui.fragment.SingleDishRemarkFragment;
import com.fangbian365.kuaidi.ui.receiver.HomeBroadcastReceiver;

public class MsgProductAdapter extends BaseSingleTypeAdapter<MsgProBean> {
	
	public MsgProductAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = LayoutInflater.from(mContext).inflate(R.layout.item_product_layout, null);
			holder.mRelativeLayout =  (RelativeLayout) contentView.findViewById(R.id.Rl_product_layout);
			holder.productPinyin = (TextView) contentView.findViewById(R.id.tv_pinyin_tag);
			holder.productName = (TextView) contentView.findViewById(R.id.product_name);
			holder.productPrince = (TextView) contentView.findViewById(R.id.product_prince);
			holder.productLow = (ImageView) contentView.findViewById(R.id.have_prod_subbt);
			holder.productAdd = (ImageView) contentView.findViewById(R.id.have_prod_addbt);
			holder.productSum = (TextView) contentView.findViewById(R.id.have_prod_tv);
			
			holder.taste = (TextView) contentView.findViewById(R.id.tv_taste);
			holder.productStatus = (TextView) contentView.findViewById(R.id.product_status);
			holder.mRelativeLayout.setOnClickListener(mOnClickListener);
			holder.productSum.setOnClickListener(mOnClickListener);
			holder.productAdd.setOnClickListener(mOnClickListener);
			holder.productLow.setOnClickListener(mOnClickListener);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		MsgProBean product = getItem(position);

		if (product != null) {
			String pin = (String)((TextUtils.isEmpty(product.getPym())) ?  "" : product.getPym().subSequence(0, 1));
			holder.productPinyin.setText(pin);
			holder.productPinyin.setBackgroundResource(SysWorkTools.getPinyinBg(pin)); //设置拼音的背景
			holder.productName.setText(product.getCodeName());
			holder.productPrince.setText("¥" + Constans.DF.format(product.getPrice()) + "" + "/"
					+ product.getUnits());
			holder.mRelativeLayout.setTag(position);
			holder.productLow.setTag(position);
			holder.productAdd.setTag(position);
			holder.productSum.setTag(position);
			holder.productSum.setText(Constans.DF.format(product.getCnt()));
			holder.taste.setText(product.getMarkTaste());
			holder.productStatus.setText(product.getWaitStatus());
		}
		return contentView;
	}
	
	@Override
	public MsgProBean getItem(int position) {
		if (mItems == null)
			return null;
		int p = position % mItems.size();
		return super.getItem(p);
	}


	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			MsgProBean product = mItems.get(position);
			switch (v.getId()) {
			case R.id.Rl_product_layout:

				Fragment f = new SingleDishRemarkFragment();
				Bundle bundle = new Bundle();
				bundle.putInt(Constans.TAKE_ORDER_POSITION, position);
				bundle.putString(Constans.WAIT_STATUS_TASTE, product.getWaitStatus());
				f.setArguments(bundle);
				
				((MainActivity) mContext).entrySubFragment(f);
				break;
			case R.id.have_prod_subbt:
					float count = product.getCnt();
					count--;
					product.setCnt((count <= 0 ? 0 : count));
					if (count <= 0) {
						mItems.remove(position);
					}
					HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_ORDER_PRO_ADD_SUB, "");
				break;
			case R.id.have_prod_tv:
				DialogFragment dialog = new InputProNumDialogFragment();
				Bundle b = new Bundle();
				b.putFloat(Constans.KEY_PRO_CNT, product.getCnt());
				b.putInt(Constans.KEY_PRO_POSITION, position);
				dialog.setArguments(b);
				dialog.show(((MainActivity) mContext).getSupportFragmentManager(), "DialogFragment");
				break;
			case R.id.have_prod_addbt:
					float count1 = product.getCnt();
					if (count1 < 100) {
						count1++;
					}
					product.setCnt(count1);// 修改数量
					
					HomeBroadcastReceiver.onDataTransfer(Constans.TYPE_ORDER_PRO_ADD_SUB, "");
				break;
			default:
				break;
			}
			
		}
	};
	
	class ViewHolder {
		
		RelativeLayout mRelativeLayout;//整个布局
		TextView productPinyin;//拼音首字母
		TextView productName; //菜品名称
		TextView productPrince; //菜品价格
		TextView productSum; //菜品数量
		ImageView productLow; //减号按钮
		ImageView productAdd; //加号按钮
		TextView taste; //口味
		TextView productStatus; //菜品状态

	}

}
