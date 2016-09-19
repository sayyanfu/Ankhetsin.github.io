package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	public ProductCategory() {
		tableList = new ArrayList<Canyin_Shop_Product>();
	}

	private ArrayList<Canyin_Shop_Product> tableList;

	public ArrayList<Canyin_Shop_Product> getTableList() {
		return tableList;
	}
}