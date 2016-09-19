package com.fangbian365.kuaidi.base.bean;
/**
 * 登录后获取的数据
 * @author Anqi
 */
public class UserInfo {
	
	private String workerId;
	private String workerNum;
	private String workerName;
	private String duty;
	private String deptCode;
	private String deptName;
	private String key;
	private String mlFlag;
	private String disCountFlag;
	private String authCode;

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public String getWorkerId() {
		return this.workerId;
	}

	public void setWorkerNum(String workerNum) {
		this.workerNum = workerNum;
	}

	public String getWorkerNum() {
		return this.workerNum;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getWorkerName() {
		return this.workerName;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getDuty() {
		return this.duty;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}
	public String getMlFlag() {
		return mlFlag;
	}

	public void setMlFlag(String mlFlag) {
		this.mlFlag = mlFlag;
	}
	public String getDisCountFlag() {
		return disCountFlag;
	}

	public void setDisCountFlag(String disCountFlag) {
		this.disCountFlag = disCountFlag;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}


}
