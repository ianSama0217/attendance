package com.example.attendance.vo;

import com.example.attendance.entity.Employee;

public class EmployeeCreateReq extends Employee {
	private String creatorId;

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

}
