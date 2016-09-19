/**
 * 赠送原因
 */

package com.fangbian365.kuaidi.base.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "canyin_shop_zsreson")
public class Canyin_Shop_Zsreson {
	@Column(name = "guid")
	private String guid;
	@Column(name = "bh")
	private String bh;
	@Column(name = "zsreson")
	private String zsreson;
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

	// zsreson
	public void setZsreson(String zsreson) {
		this.zsreson = zsreson;
	}

	public String getZsreson() {
		return zsreson;
	}

	// remark
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}


	@Override
	public String toString() {
		return "Canyin_Shop_Zsreson [guid=" + guid + ", bh=" + bh
				+ ", zsreson=" + zsreson + ", remark=" + remark
				+ ", isChoosed=" + isChoosed + "]";
	}

}