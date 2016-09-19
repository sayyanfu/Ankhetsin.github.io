package com.fangbian365.kuaidi.ui.uisupport;

import java.util.ArrayList;

import com.fangbian365.kuaidi.base.bean.HaveProductBean;

public class ProductList<T> extends ArrayList<HaveProductBean> {

	private static final long serialVersionUID = 1L;

	public boolean add(HaveProductBean e, int type) {
		for (int i = 0; i < super.size(); i++) {
			HaveProductBean d = super.get(i);
			if (e.getCode().equals(d.getCode())) {
				if(type == 0) {
					if (e.getCnt() == 0) {
						super.remove(d);
						return false;
					} else {
						d.setCnt(e.getCnt());
						return true;
					}
				} else {
					float cnt = d.getCnt() + e.getCnt();
					d.setCnt(cnt);
					return true;
				}
			}
		}
		HaveProductBean bean = (HaveProductBean) e.clone();
		if (bean.getCnt() == 0) {
			return false;
		}
		super.add(0, bean);
		return true;
	}

}
