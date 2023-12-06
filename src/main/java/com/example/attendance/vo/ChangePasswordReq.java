package com.example.attendance.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordReq {

	private String id;

	@JsonProperty("old_password")
	private String oldPwd;

	@JsonProperty("auth_code")
	private String authCode;

	@JsonProperty("new_password")
	private String newPwd;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

}
