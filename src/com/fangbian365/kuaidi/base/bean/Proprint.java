/**
 * 菜品和打印机配置表
 */

package com.fangbian365.kuaidi.base.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "proprint")
public class Proprint {
	@Column(name = "ID")
	private Integer ID;
	@Column(name = "ProCode", isId = true)
	private String ProCode;
	@Column(name = "FCPrintCode")
	private String FCPrintCode;
	@Column(name = "ZCPrintCode")
	private String ZCPrintCode;
	@Column(name = "PCPrintCode")
	private String PCPrintCode;
	@Column(name = "FCPrintCode2")
	private String FCPrintCode2;

	public String getFCPrintCode2() {
		return FCPrintCode2;
	}

	public void setFCPrintCode2(String fCPrintCode2) {
		FCPrintCode2 = fCPrintCode2;
	}

	// ID
	public void setId(Integer id) {
		this.ID = id;
	}

	public Integer getId() {
		return ID;
	}

	// ProCode
	public void setProcode(String procode) {
		this.ProCode = procode;
	}

	public String getProcode() {
		return ProCode;
	}

	// FCPrintCode
	public void setFcprintcode(String fcprintcode) {
		this.FCPrintCode = fcprintcode;
	}

	public String getFcprintcode() {
		return FCPrintCode;
	}

	// ZCPrintCode
	public void setZcprintcode(String zcprintcode) {
		this.ZCPrintCode = zcprintcode;
	}

	public String getZcprintcode() {
		return ZCPrintCode;
	}

	// PCPrintCode
	public void setPcprintcode(String pcprintcode) {
		this.PCPrintCode = pcprintcode;
	}

	public String getPcprintcode() {
		return PCPrintCode;
	}

}