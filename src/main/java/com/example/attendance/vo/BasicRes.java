package com.example.attendance.vo;

import com.example.attendance.constants.RtnCode;

public class BasicRes {

	private RtnCode rtnCode;

	public BasicRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BasicRes(RtnCode rtnCode) {
		super();
		this.rtnCode = rtnCode;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}

}
