package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;

public class DevicePwd implements Serializable {

	private static final long serialVersionUID = -7726425827499414283L;
	private String devPwd;
	public String getDevPwd() {
		return devPwd;
	}
	public void setDevPwd(String devPwd) {
		this.devPwd = devPwd;
	}
	@Override
	public String toString() {
		return "DevicePwd [devPwd=" + devPwd + "]";
	}

}
