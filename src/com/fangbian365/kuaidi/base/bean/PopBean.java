package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;

public class PopBean  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String data = "";
	public PopBean(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	

	@Override
	public boolean equals(Object obj) {
		boolean bres = false;
	    if (obj instanceof PopBean) {
	    	PopBean o = (PopBean) obj;
	        bres = this.data.equals(o.getData());
	    }
	    return bres;
	}
	
}
