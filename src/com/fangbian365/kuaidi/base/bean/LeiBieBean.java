package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;

import com.fangbian365.kuaidi.base.utils.PrefsConfig;

public class LeiBieBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String smallCode;
	private String smallName;
	private Float maxDis;
	private Float disRate = 1.0f;

	private String productName;
	private Double price;
	private Float cnt;

	private String tingbh;
	private String taibh;
	
	public LeiBieBean() {}
	public LeiBieBean(HaveProductBean product) {
		this.productName = product.getCodeName();
		this.price = product.getPrice();
		this.cnt = product.getCnt();
//		this.maxDis = Float.parseFloat(PrefsConfig.disCountFlag);
//		this.disRate = Float.parseFloat(PrefsConfig.disCountFlag);
		
		this.maxDis = Float.parseFloat(PrefsConfig.disCountFlag);
		this.disRate = 1.0f;
		this.tingbh = product.getTingBh();
		this.taibh = product.getTaiBh();
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSmallCode() {
		return smallCode;
	}

	public void setSmallCode(String smallCode) {
		this.smallCode = smallCode;
	}

	public String getSmallName() {
		return smallName;
	}

	public void setSmallName(String smallName) {
		this.smallName = smallName;
	}

	public Float getMaxDis() {
		return maxDis;
	}

	public void setMaxDis(Float maxDis) {
		this.maxDis = maxDis;
	}

	public Float getDisRate() {
		return disRate;
	}

	public void setDisRate(Float disRate) {
		this.disRate = disRate;
	}
	
	public Float getCnt() {
		return cnt;
	}
	public void setCnt(Float cnt) {
		this.cnt = cnt;
	}
	
	public String getTingbh() {
		return tingbh;
	}
	public void setTingbh(String tingbh) {
		this.tingbh = tingbh;
	}
	public String getTaibh() {
		return taibh;
	}
	public void setTaibh(String taibh) {
		this.taibh = taibh;
	}

}
