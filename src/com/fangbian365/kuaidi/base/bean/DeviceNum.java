package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;

public class DeviceNum implements Serializable {

	private static final long serialVersionUID = -7726425827499414283L;
	private String devNum;
	public String getDevNum() {
		return devNum;
	}
	public void setDevNum(String devNum) {
		this.devNum = devNum;
	}
	@Override
	public String toString() {
		return "DeviceNum [devNum=" + devNum + "]";
	}
}
