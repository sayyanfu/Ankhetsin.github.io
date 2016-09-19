package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;
/**
 * 催菜
 */
public class RemindDish implements Serializable {

	private static final long serialVersionUID = 6416715172837755135L;
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWaitstatus() {
		return waitstatus;
	}
	public void setWaitstatus(String waitstatus) {
		this.waitstatus = waitstatus;
	}
	private String waitstatus;
	
}
