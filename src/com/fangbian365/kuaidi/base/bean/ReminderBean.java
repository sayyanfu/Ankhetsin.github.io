package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;
import java.util.List;

public class ReminderBean implements Serializable {
	private static final long serialVersionUID = 9183166141046376902L;

	private String startTime;

	private String lsh;

	private String reminderNum;

	private String taiBh;

	private List<ReminderProducts> products;

	private String opername;

	private String taiName;

	private String tingName;

	private String opercode;

	private String tingBh;

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setLsh(String lsh) {
		this.lsh = lsh;
	}

	public String getLsh() {
		return this.lsh;
	}

	public void setReminderNum(String reminderNum) {
		this.reminderNum = reminderNum;
	}

	public String getReminderNum() {
		return this.reminderNum;
	}

	public void setTaiBh(String taiBh) {
		this.taiBh = taiBh;
	}

	public String getTaiBh() {
		return this.taiBh;
	}

	public void setProducts(List<ReminderProducts> products) {
		this.products = products;
	}

	public List<ReminderProducts> getProducts() {
		return this.products;
	}

	public void setOpername(String opername) {
		this.opername = opername;
	}

	public String getOpername() {
		return this.opername;
	}

	public void setTaiName(String taiName) {
		this.taiName = taiName;
	}

	public String getTaiName() {
		return this.taiName;
	}

	public void setTingName(String tingName) {
		this.tingName = tingName;
	}

	public String getTingName() {
		return this.tingName;
	}

	public void setOpercode(String opercode) {
		this.opercode = opercode;
	}

	public String getOpercode() {
		return this.opercode;
	}

	public void setTingBh(String tingBh) {
		this.tingBh = tingBh;
	}

	public String getTingBh() {
		return this.tingBh;
	}
}
