package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;

import org.xutils.db.annotation.Column;

public class MsgProBean implements Serializable , Cloneable  {
	
	public MsgProBean() {
	}

	public MsgProBean(Canyin_Shop_Product product,Canyin_Shop_Diningtable table) {
		if (table != null) {
			this.tingBh = table.getFloorcode();
			this.taiBh = table.getCode();
			this.lsh = table.getLsh();
		}
		this.code = product.getProductcode();
		this.codeName = product.getProductname();
		this.orderState = 0;
		this.cnt = 1.0f;
		this.units = product.getUnits();
		this.price = product.getOriginalprice();
		this.dishTotalPrice = String.valueOf(product.getOriginalprice());
		this.waiter = "";
		this.markTaste = "";
		this.status = 1;
		this.returnStatus = 0;
		this.disflag = product.getDisflag();
		this.memberPrice = product.getMemberprice();
		this.actPrice = product.getCurrentprice();
		this.waitStatus = "即起";
		this.priceSrc = product.getOriginalprice();
		this.returnStatus = 0;
		this.discound = String.valueOf(product.getDiscound());
		this.pym = product.getPinyin();
		this.markStatus = "";
	}
	private String id;

	private String shopId;

	private String lslsh;

	private String posBh;

	private String code;

	private String codeName;

	private String units;

	private Double price;

	private Double memberPrice;

	private Double actPrice;

	private Float cnt;

	private String disFlag;

	private int status;

	private String pym;

	private String bigCode;

	private String bigName;

	private String smallCode;

	private String smallName;

	private String markTaste;

	private String jdType;

	private String dis;

	private Double priceSrc;

	private String startTime;
	private String waitStatus;// 起叫状态
	
	private String taiBh;
	private String tingBh;
	private String lsh;
	private int orderState;// 下单状态(0未下单，1已下单)
	private String waiter;// 服务员
	private String givedish;// 增菜
	private int returnStatus;// 退菜
	private int disflag;// 是否参与折扣0-不参与 1参与
	private String discound;// 折扣
	private String dishTotalPrice;// 菜品小计
	private String markStatus;
	private String dcyname;
	private String dcycode;
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopId() {
		return this.shopId;
	}

	public void setLslsh(String lslsh) {
		this.lslsh = lslsh;
	}

	public String getLslsh() {
		return this.lslsh;
	}

	public void setPosBh(String posBh) {
		this.posBh = posBh;
	}

	public String getPosBh() {
		return this.posBh;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getCodeName() {
		return this.codeName;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getUnits() {
		return this.units;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(Double memberPrice) {
		this.memberPrice = memberPrice;
	}

	public Double getActPrice() {
		return actPrice;
	}

	public void setActPrice(Double actPrice) {
		this.actPrice = actPrice;
	}

	public Float getCnt() {
		return cnt;
	}

	public void setCnt(Float cnt) {
		this.cnt = cnt;
	}

	public void setDisFlag(String disFlag) {
		this.disFlag = disFlag;
	}

	public String getDisFlag() {
		return this.disFlag;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return this.status;
	}

	public void setPym(String pym) {
		this.pym = pym;
	}

	public String getPym() {
		return this.pym;
	}

	public void setBigCode(String bigCode) {
		this.bigCode = bigCode;
	}

	public String getBigCode() {
		return this.bigCode;
	}

	public void setBigName(String bigName) {
		this.bigName = bigName;
	}

	public String getBigName() {
		return this.bigName;
	}

	public void setSmallCode(String smallCode) {
		this.smallCode = smallCode;
	}

	public String getSmallCode() {
		return this.smallCode;
	}

	public void setSmallName(String smallName) {
		this.smallName = smallName;
	}

	public String getSmallName() {
		return this.smallName;
	}

	public void setMarkTaste(String markTaste) {
		this.markTaste = markTaste;
	}

	public String getMarkTaste() {
		return this.markTaste;
	}

	public void setJdType(String jdType) {
		this.jdType = jdType;
	}

	public String getJdType() {
		return this.jdType;
	}

	public void setDis(String dis) {
		this.dis = dis;
	}

	public String getDis() {
		return this.dis;
	}

	public void setPriceSrc(Double priceSrc) {
		this.priceSrc = priceSrc;
	}

	public Double getPriceSrc() {
		return this.priceSrc;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartTime() {
		return this.startTime;
	}
	
	public String getWaitStatus() {
		return waitStatus;
	}

	public void setWaitStatus(String waitStatus) {
		this.waitStatus = waitStatus;
	}

	public String getTaiBh() {
		return taiBh;
	}

	public void setTaiBh(String taiBh) {
		this.taiBh = taiBh;
	}

	public String getTingBh() {
		return tingBh;
	}

	public void setTingBh(String tingBh) {
		this.tingBh = tingBh;
	}

	public String getLsh() {
		return lsh;
	}

	public void setLsh(String lsh) {
		this.lsh = lsh;
	}

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public String getWaiter() {
		return waiter;
	}

	public void setWaiter(String waiter) {
		this.waiter = waiter;
	}

	public String getGivedish() {
		return givedish;
	}

	public void setGivedish(String givedish) {
		this.givedish = givedish;
	}

	public int getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(int returnStatus) {
		this.returnStatus = returnStatus;
	}

	public int getDisflag() {
		return disflag;
	}

	public void setDisflag(int disflag) {
		this.disflag = disflag;
	}

	public String getDiscound() {
		return discound;
	}

	public void setDiscound(String discound) {
		this.discound = discound;
	}

	public String getDishTotalPrice() {
		return dishTotalPrice;
	}

	public void setDishTotalPrice(String dishTotalPrice) {
		this.dishTotalPrice = dishTotalPrice;
	}

	public String getMarkStatus() {
		return markStatus;
	}

	public void setMarkStatus(String markStatus) {
		this.markStatus = markStatus;
	}

	public String getDcyname() {
		return dcyname;
	}

	public void setDcyname(String dcyname) {
		this.dcyname = dcyname;
	}

	public String getDcycode() {
		return dcycode;
	}

	public void setDcycode(String dcycode) {
		this.dcycode = dcycode;
	}

	@Override
	public Object clone() {
		MsgProBean bean = null;
		try {
			bean = (MsgProBean) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MsgProBean) {
			MsgProBean t = (MsgProBean) obj;
			if (this.code.equals(t.getCode())) {
				return true;
			}
		}
		return false;
	}


}
