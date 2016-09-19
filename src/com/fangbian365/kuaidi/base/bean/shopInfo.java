package com.fangbian365.kuaidi.base.bean;

public class shopInfo {

	private static final long serialVersionUID = 346646082715881953L;
	private String shopId;
	private String key;
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@Override
	public String toString() {
		return "shopInfo [shopId=" + shopId + ", key=" + key + "]";
	}

}
