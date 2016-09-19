package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	public Category() {
		tableList = new ArrayList<Canyin_Shop_Diningtable>();
	}

	private ArrayList<Canyin_Shop_Diningtable> tableList;

	public ArrayList<Canyin_Shop_Diningtable> getTableList() {
		return tableList;
	}
}