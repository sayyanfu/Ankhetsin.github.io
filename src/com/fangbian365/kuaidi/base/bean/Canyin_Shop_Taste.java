/**
 * 口味
 */

package com.fangbian365.kuaidi.base.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "canyin_shop_taste")
public class Canyin_Shop_Taste {
	@Column(name = "tasteid")
	private String tasteid;
	@Column(name = "tasteCode")
	private String tasteCode;
	@Column(name = "tasteName")
	private String tasteName;
	private boolean isChoosed = false;
	
	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}
	
	// tasteid
	public void setTasteid(String tasteid) {
		this.tasteid = tasteid;
	}

	public String getTasteid() {
		return tasteid;
	}

	// tasteCode
	public void setTastecode(String tastecode) {
		this.tasteCode = tastecode;
	}

	public String getTastecode() {
		return tasteCode;
	}

	// tasteName
	public void setTastename(String tastename) {
		this.tasteName = tastename;
	}

	public String getTastename() {
		return tasteName;
	}

	@Override
	public String toString() {
		return "Canyin_Shop_Taste [tasteid=" + tasteid + ", tasteCode="
				+ tasteCode + ", tasteName=" + tasteName + "]";
	}

}