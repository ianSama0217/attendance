package com.example.attendance.vo;

import java.util.List;

import com.example.attendance.entity.Departments;

public class DepartmentsCreateReq {

	List<Departments> departments;

	public DepartmentsCreateReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DepartmentsCreateReq(List<Departments> departments) {
		super();
		this.departments = departments;
	}

	public List<Departments> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Departments> departments) {
		this.departments = departments;
	}

}
