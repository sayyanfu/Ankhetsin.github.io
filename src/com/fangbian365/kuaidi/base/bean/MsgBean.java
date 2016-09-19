package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;

public class MsgBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String tasteRemark;
	private String startTime;
	private String id;
	private String lslsh;
	private Integer status;
	private String shopId;
	private String mealName;
	private Float priceTotal;
	private String ktlsh;
	private String pos;
	private String tingBH;
	private String taiBH;
	private String tingName;
	private String taiName;
	
	
	
	public String getTingName() {
		return tingName;
	}
	public void setTingName(String tingName) {
		this.tingName = tingName;
	}
	public String getTaiName() {
		return taiName;
	}
	public void setTaiName(String taiName) {
		this.taiName = taiName;
	}
	public String getTasteRemark() {
		return tasteRemark;
	}
	public void setTasteRemark(String tasteRemark) {
		this.tasteRemark = tasteRemark;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLslsh() {
		return lslsh;
	}
	public void setLslsh(String lslsh) {
		this.lslsh = lslsh;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getMealName() {
		return mealName;
	}
	public void setMealName(String mealName) {
		this.mealName = mealName;
	}
	public Float getPriceTotal() {
		return priceTotal;
	}
	public void setPriceTotal(Float priceTotal) {
		this.priceTotal = priceTotal;
	}
	public String getKtlsh() {
		return ktlsh;
	}
	public void setKtlsh(String ktlsh) {
		this.ktlsh = ktlsh;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getTingBH() {
		return tingBH;
	}
	public void setTingBH(String tingBH) {
		this.tingBH = tingBH;
	}
	public String getTaiBH() {
		return taiBH;
	}
	public void setTaiBH(String taiBH) {
		this.taiBH = taiBH;
	}
	@Override
	public String toString() {
		return "MsgBean [tasteRemark=" + tasteRemark + ", startTime="
				+ startTime + ", id=" + id + ", lslsh=" + lslsh + ", status="
				+ status + ", shopId=" + shopId + ", mealName=" + mealName
				+ ", priceTotal=" + priceTotal + ", ktlsh=" + ktlsh + ", pos="
				+ pos + ", tingBH=" + tingBH + ", taiBH=" + taiBH + "]";
	}
	
	

}
