package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;
/**
 * 超级密码
 */
public class RootPwd implements Serializable {

	private static final long serialVersionUID = 7985385897248369318L;
	private String rootPwd;
	public String getRootPwd() {
		return rootPwd;
	}
	public void setRootPwd(String rootPwd) {
		this.rootPwd = rootPwd;
	}
}
