package com.fangbian365.kuaidi.base.bean;

public class GiveDishDataItemBean {
	String id;
	float cnt;
	String zscnt;
	int zscode;
	String zsname;
	String zsycode;
	String zsyname;
	String netid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public float getCnt() {
		return cnt;
	}
	public void setCnt(float cnt) {
		this.cnt = cnt;
	}
	public String getZscnt() {
		return zscnt;
	}
	public void setZscnt(String zscnt) {
		this.zscnt = zscnt;
	}
	public int getZscode() {
		return zscode;
	}
	public void setZscode(int zscode) {
		this.zscode = zscode;
	}
	public String getZsname() {
		return zsname;
	}
	public void setZsname(String zsname) {
		this.zsname = zsname;
	}
	public String getZsycode() {
		return zsycode;
	}
	public void setZsycode(String zsycode) {
		this.zsycode = zsycode;
	}
	public String getZsyname() {
		return zsyname;
	}
	public void setZsyname(String zsyname) {
		this.zsyname = zsyname;
	}
	public String getNetid() {
		return netid;
	}
	public void setNetid(String netid) {
		this.netid = netid;
	}
	@Override
	public String toString() {
		return "GiveDishDataItemBean [id=" + id + ", cnt=" + cnt + ", zscnt="
				+ zscnt + ", zscode=" + zscode + ", zsname=" + zsname
				+ ", zsycode=" + zsycode + ", zsyname=" + zsyname + ", netid="
				+ netid + "]";
	}
	
	
	
}