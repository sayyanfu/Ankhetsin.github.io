/**
 * 餐段
 */

package com.fangbian365.kuaidi.base.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "canyin_shop_mealstime")
public class Canyin_Shop_Mealstime {

	@Column(name = "guid")
	private String guid;
	@Column(name = "shopId")
	private String shopId;
	@Column(name = "code")
	private String code;
	@Column(name = "mealName")
	private String mealName;
	@Column(name = "startTime")
	private String startTime;
	@Column(name = "endTime")
	private String endTime;

	// guid
	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getGuid() {
		return guid;
	}

	// shopId
	public void setShopid(String shopid) {
		this.shopId = shopid;
	}

	public String getShopid() {
		return shopId;
	}

	// code
	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	// mealName
	public void setMealname(String mealname) {
		this.mealName = mealname;
	}

	public String getMealname() {
		return mealName;
	}

	// startTime
	public void setStarttime(String starttime) {
		this.startTime = starttime;
	}

	public String getStarttime() {
		return startTime;
	}

	// endTime
	public void setEndtime(String endtime) {
		this.endTime = endtime;
	}

	public String getEndtime() {
		return endTime;
	}

}