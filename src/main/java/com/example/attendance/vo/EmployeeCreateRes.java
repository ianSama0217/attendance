package com.example.attendance.vo;

import com.example.attendance.constants.RtnCode;

public class EmployeeCreateRes {

	private RtnCode rtnCode;

	public EmployeeCreateRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmployeeCreateRes(RtnCode rtnCode) {
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
