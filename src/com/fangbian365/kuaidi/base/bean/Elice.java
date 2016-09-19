package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;

/**
 * 划菜对象
 */
public class Elice implements Serializable{
	private static final long serialVersionUID = -246523710023934139L;
	private String id;
	private String markstatus;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMarkstatus() {
		return markstatus;
	}
	public void setMarkstatus(String markstatus) {
		this.markstatus = markstatus;
	}
	@Override
	public String toString() {
		return "Elice [id=" + id + ", markstatus=" + markstatus + "]";
	}
}
