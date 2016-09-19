package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;

public class DownloadItem implements Serializable {

	private static final long serialVersionUID = 1678671044380995944L;

	private String url;
	private long currSize;
	private String name;
	private String id ;
	private String type;
	private String edition;
	private long Byte;
	private String Explain;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getCurrSize() {
		return currSize;
	}
	public void setCurrSize(long currSize) {
		this.currSize = currSize;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public long getByte() {
		return Byte;
	}
	public void setByte(long b) {
		Byte = b;
	}
	public String getExplain() {
		return Explain;
	}
	public void setExplain(String explain) {
		Explain = explain;
	}

	
}
