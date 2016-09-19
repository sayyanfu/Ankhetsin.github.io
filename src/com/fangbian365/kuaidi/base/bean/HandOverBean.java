package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;
/**
 * 交班Bean
 */
public class HandOverBean implements Serializable {
	
	private static final long serialVersionUID = 112L;
	private String sbsj; //登录时间
	private String totalPrice; //原价合计
	private String preferentialTj; //优惠合计
	private String payName1; //支付方式1
	private String payMoneyTj1; //支付金额1
	private String payName2; //支付方式2
	private String payMoneyTj2; //支付金额2
	private String smallName; //类别名称
	private String priceTj; //原价合计
	private String sjPriceTj; //实收合计
	private String floorName1; //厅1名称
	private String floorPrice1; //厅1价格
	private String floorName2; //厅2名称
	private String floorPrice2; //厅2价格
	private String consumptionNum; //消费人次
	private String consumpAmount; //消费金额
	private String rechargeAmount; //充值金额
	private String refundAmount; //退款金额
	private String checkoutNum; //结账次数
	private String personCntTj; //客人总数
	private String giftAmount; //赠单金额
	private String discountAmount; //折扣金额
	private String malingAmount; //抹零金额
	private String tcAmount; //退菜金额
	private String turnovert; //实收总额
	
	private int type = 1;//
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getSbsj() {
		return sbsj;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public String getPreferentialTj() {
		return preferentialTj;
	}

	public String getSmallName() {
		return smallName;
	}
	public String getPriceTj() {
		return priceTj;
	}
	public String getSjPriceTj() {
		return sjPriceTj;
	}
	public String getFloorName1() {
		return floorName1;
	}
	public String getFloorPrice1() {
		return floorPrice1;
	}
	public String getFloorName2() {
		return floorName2;
	}
	public String getFloorPrice2() {
		return floorPrice2;
	}
	public String getConsumptionNum() {
		return consumptionNum;
	}
	public String getConsumpAmount() {
		return consumpAmount;
	}
	public String getRechargeAmount() {
		return rechargeAmount;
	}
	public String getRefundAmount() {
		return refundAmount;
	}
	public String getCheckoutNum() {
		return checkoutNum;
	}
	public String getPersonCntTj() {
		return personCntTj;
	}
	public String getGiftAmount() {
		return giftAmount;
	}
	public String getDiscountAmount() {
		return discountAmount;
	}
	public String getMalingAmount() {
		return malingAmount;
	}
	public String getTcAmount() {
		return tcAmount;
	}
	public String getTurnovert() {
		return turnovert;
	}
	public void setSbsj(String sbsj) {
		this.sbsj = sbsj;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public void setPreferentialTj(String preferentialTj) {
		this.preferentialTj = preferentialTj;
	}

	public void setSmallName(String smallName) {
		this.smallName = smallName;
	}
	public void setPriceTj(String priceTj) {
		this.priceTj = priceTj;
	}
	public void setSjPriceTj(String sjPriceTj) {
		this.sjPriceTj = sjPriceTj;
	}
	public void setFloorName1(String floorName1) {
		this.floorName1 = floorName1;
	}
	public void setFloorPrice1(String floorPrice1) {
		this.floorPrice1 = floorPrice1;
	}
	public void setFloorName2(String floorName2) {
		this.floorName2 = floorName2;
	}
	public void setFloorPrice2(String floorPrice2) {
		this.floorPrice2 = floorPrice2;
	}
	public void setConsumptionNum(String consumptionNum) {
		this.consumptionNum = consumptionNum;
	}
	public void setConsumpAmount(String consumpAmount) {
		this.consumpAmount = consumpAmount;
	}
	public void setRechargeAmount(String rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	public void setCheckoutNum(String checkoutNum) {
		this.checkoutNum = checkoutNum;
	}
	public void setPersonCntTj(String personCntTj) {
		this.personCntTj = personCntTj;
	}
	public void setGiftAmount(String giftAmount) {
		this.giftAmount = giftAmount;
	}
	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}
	public void setMalingAmount(String malingAmount) {
		this.malingAmount = malingAmount;
	}
	public void setTcAmount(String tcAmount) {
		this.tcAmount = tcAmount;
	}
	public void setTurnovert(String turnovert) {
		this.turnovert = turnovert;
	}
	public String getPayName1() {
		return payName1;
	}
	public String getPayMoneyTj1() {
		return payMoneyTj1;
	}
	public String getPayName2() {
		return payName2;
	}
	public String getPayMoneyTj2() {
		return payMoneyTj2;
	}
	public void setPayName1(String payName1) {
		this.payName1 = payName1;
	}
	public void setPayMoneyTj1(String payMoneyTj1) {
		this.payMoneyTj1 = payMoneyTj1;
	}
	public void setPayName2(String payName2) {
		this.payName2 = payName2;
	}
	public void setPayMoneyTj2(String payMoneyTj2) {
		this.payMoneyTj2 = payMoneyTj2;
	}
}
