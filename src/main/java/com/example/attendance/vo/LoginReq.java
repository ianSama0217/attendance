package com.example.attendance.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginReq {

	private String id;

	@JsonProperty("password")
	private String pwd;

	public LoginReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
