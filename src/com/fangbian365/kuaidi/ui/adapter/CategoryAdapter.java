package com.fangbian365.kuaidi.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangbian365.kuaidi.R;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Product_Type;
import com.fangbian365.kuaidi.ui.adapter.base.BaseSingleTypeAdapter;

public class CategoryAdapter extends
		BaseSingleTypeAdapter<Canyin_Shop_Product_Type> {

	private Context context;
	private List<Canyin_Shop_Product_Type> mList;
	public CategoryAdapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = LayoutInflater.from(context).inflate(
					R.layout.item_category_layout, null);
			holder.categoryName = (TextView) contentView
					.findViewById(R.id.category_name);

			holder.categoryRela = (RelativeLayout) contentView
					.findViewById(R.id.category_rela);

			holder.categoryRela.setTag(position);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		Canyin_Shop_Product_Type category = getItem(position);

		holder.categoryName.setText(category.getTypename());

		if (category.isClick()) {
			holder.categoryName.setTextColor(mContext.getResources().getColor(
					R.color.category_pass_color));
		} else {
			holder.categoryName.setTextColor(mContext.getResources().getColor(
					R.color.et_main_search_hint));
		}

		return contentView;
	}

	class ViewHolder {
		RelativeLayout categoryRela;
		TextView categoryName;

	}

}
