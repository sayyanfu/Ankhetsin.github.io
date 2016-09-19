package com.fangbian365.kuaidi.base.bean;

import java.util.Date;

import android.util.Base64;

import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;

/**
 * 
 */
public class DCaiBean {
	
	private String code;
	private Float cnt;
	private Double price;
	private String units;
	private int status;// 1正常 2赠菜
	private String taste;
	private String dcycode;
	private String dcyname;
	private Integer disflag;
	private Double memberPrice;
	private Double actPrice;
	private String waitStatus;
	private Double priceSrc;
	private String addLsh = "";
	

	public DCaiBean(){
	}
	
	public DCaiBean(HaveProductBean product) {
		this.code = product.getCode();
		this.cnt = product.getCnt();
		this.price = product.getPrice();
		this.units = product.getUnits();
		this.status = product.getStatus();
		this.taste = product.getMarkTaste();
		this.dcycode = PrefsConfig.workerNum;
		this.dcyname = PrefsConfig.workerName;
		this.disflag = product.getDisflag();
		this.memberPrice = product.getMemberPrice();
		this.waitStatus = product.getWaitStatus();
		this.actPrice = product.getActPrice(); 
		this.priceSrc = product.getPriceSrc();
		String devNum = new String(Base64.decode(PrefsConfig.devPwd,Base64.DEFAULT));
		this.addLsh = devNum + String.valueOf(System.currentTimeMillis());
	}
	
	public DCaiBean(MsgProBean product) {
		this.code = product.getCode();
		this.cnt = product.getCnt();
		this.price = product.getPrice();
		this.units = product.getUnits();
		this.status = product.getStatus();
		this.taste = product.getMarkTaste();
		this.dcycode = PrefsConfig.workerNum;
		this.dcyname = PrefsConfig.workerName;
		this.disflag = product.getDisflag();
		this.memberPrice = product.getMemberPrice();
		this.waitStatus = product.getWaitStatus();
		this.actPrice = product.getActPrice(); 
		this.priceSrc = product.getPriceSrc();
		String devNum = new String(Base64.decode(PrefsConfig.devPwd,Base64.DEFAULT));
		this.addLsh = devNum + String.valueOf(System.currentTimeMillis());
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Float getCnt() {
		return cnt;
	}

	public void setCnt(Float cnt) {
		this.cnt = cnt;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTaste() {
		return taste;
	}

	public void setTaste(String taste) {
		this.taste = taste;
	}

	public String getDcycode() {
		return dcycode;
	}

	public void setDcycode(String dcycode) {
		this.dcycode = dcycode;
	}

	public String getDcyname() {
		return dcyname;
	}

	public void setDcyname(String dcyname) {
		this.dcyname = dcyname;
	}


	public String getWaitStatus() {
		return waitStatus;
	}

	public void setWaitStatus(String waitStatus) {
		this.waitStatus = waitStatus;
	}

	public Integer getDisflag() {
		return disflag;
	}

	public void setDisflag(Integer disflag) {
		this.disflag = disflag;
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

	public Double getPriceSrc() {
		return priceSrc;
	}

	public void setPriceSrc(Double priceSrc) {
		this.priceSrc = priceSrc;
	}
	
	public String getAddLsh() {
		return addLsh;
	}

	public void setAddLsh(String addLsh) {
		this.addLsh = addLsh;
	}
	
}