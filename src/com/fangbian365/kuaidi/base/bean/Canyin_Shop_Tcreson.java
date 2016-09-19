/**
 * 退菜说明
 *
 */

package com.fangbian365.kuaidi.base.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "canyin_shop_tcreson")
public class Canyin_Shop_Tcreson {

	@Column(name = "guid")
	private String guid;
	@Column(name = "bh")
	private String bh;
	@Column(name = "reasonDescription")
	private String reasonDescription;
	@Column(name = "remark")
	private String remark;
	private boolean isChoosed = false;
	
	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}

	// guid
	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getGuid() {
		return guid;
	}

	// bh
	public void setBh(String bh) {
		this.bh = bh;
	}

	public String getBh() {
		return bh;
	}


	// remark
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public String getReasonDescription() {
		return reasonDescription;
	}

	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}


	
}