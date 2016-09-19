/**
 * 座位
 */

package com.fangbian365.kuaidi.base.bean;

import java.util.Date;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "canyin_shop_diningtable")
public class Canyin_Shop_Diningtable extends PopBean {

	private static final long serialVersionUID = 1875454549415852487L;

	public Canyin_Shop_Diningtable() {
		super("");
	}

	public Canyin_Shop_Diningtable(Canyin_Shop_Diningtable table) {
		super("");

		this.guid = table.getGuid();
		this.dTableName = table.getDtablename();
		this.isRoom = table.getIsroom();
		this.floorcode = table.getFloorcode();
		this.floorName = table.getFloorName();
		this.maxSeat = table.getMaxseat();
		this.canBooking = table.getCanbooking();
		this.serviceCost = table.getServicecost();
		this.minSpending = table.getMinspending();
		this.remark = table.getRemark();
		this.code = table.getCode();
		this.status = table.getStatus();
		this.starttime = table.getStarttime();
		this.lsh = table.getLsh();
		this.personCnt = table.getPersoncnt();
		this.isChoosed = table.isChoosed();
	}

	@Column(name = "id", isId = true)
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "guid")
	private String guid;
	@Column(name = "dTableName")
	private String dTableName;
	@Column(name = "isRoom")
	// 1大厅 2包间 3雅间，4卡座，5外卖
	private Integer isRoom;
	@Column(name = "floorcode")
	private String floorcode;
	private String floorName;
	@Column(name = "maxSeat")
	private Integer maxSeat;
	@Column(name = "canBooking")
	private Integer canBooking;
	@Column(name = "serviceCost")
	private String serviceCost;
	@Column(name = "minSpending")
	private Double minSpending;
	@Column(name = "remark")
	private String remark;
	@Column(name = "code")
	private String code;
	@Column(name = "status")
	private Integer status;// 状态(0空闲，1占用，2禁用，3已结账待清)
	@Column(name = "starttime")
	private Date starttime;
	@Column(name = "lsh")
	private String lsh;
	@Column(name = "isProduct")
	private String isProduct;
	@Column(name = "priceTotal")
	private String priceTotal;
	@Column(name = "personCnt")
	private Integer personCnt;

	private boolean isChoosed;

	public String getPriceTotal() {
		return priceTotal;
	}

	public void setPriceTotal(String priceTotal) {
		this.priceTotal = priceTotal;
	}

	// guid
	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getGuid() {
		return guid;
	}

	// dTableName
	public void setDtablename(String dtablename) {
		this.dTableName = dtablename;
	}

	public String getDtablename() {
		return dTableName;
	}

	// isRoom
	public void setIsroom(Integer isroom) {
		this.isRoom = isroom;
	}

	public Integer getIsroom() {
		return isRoom;
	}

	// floorcode
	public void setFloorcode(String floorcode) {
		this.floorcode = floorcode;
	}

	public String getFloorcode() {
		return floorcode;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	// maxSeat
	public void setMaxseat(Integer maxseat) {
		this.maxSeat = maxseat;
	}

	public Integer getMaxseat() {
		return maxSeat;
	}

	// canBooking
	public void setCanbooking(Integer canbooking) {
		this.canBooking = canbooking;
	}

	public Integer getCanbooking() {
		return canBooking;
	}

	// serviceCost
	public void setServicecost(String servicecost) {
		this.serviceCost = servicecost;
	}

	public String getServicecost() {
		return serviceCost;
	}

	// minSpending
	public void setMinspending(Double minspending) {
		this.minSpending = minspending;
	}

	public Double getMinspending() {
		return minSpending;
	}

	// remark
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	// code
	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	// status
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	// starttime
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getStarttime() {
		return starttime;
	}

	// lsh
	public void setLsh(String lsh) {
		this.lsh = lsh;
	}

	public String getLsh() {
		return lsh;
	}

	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}

	public void setPersoncnt(Integer personcnt) {
		this.personCnt = personcnt;
	}

	public Integer getPersoncnt() {
		return personCnt;
	}

	public String getIsProduct() {
		return isProduct;
	}

	public void setIsProduct(String isProduct) {
		this.isProduct = isProduct;
	}

	@Override
	public String toString() {
		return "Canyin_Shop_Diningtable [id=" + id + ", guid=" + guid
				+ ", dTableName=" + dTableName + ", isRoom=" + isRoom
				+ ", floorcode=" + floorcode + ", floorName=" + floorName
				+ ", maxSeat=" + maxSeat + ", canBooking=" + canBooking
				+ ", serviceCost=" + serviceCost + ", minSpending="
				+ minSpending + ", remark=" + remark + ", code=" + code
				+ ", status=" + status + ", starttime=" + starttime + ", lsh="
				+ lsh + ", isProduct=" + isProduct + ", priceTotal="
				+ priceTotal + ", personCnt=" + personCnt + ", isChoosed="
				+ isChoosed + "]";
	}

}