package com.example.attendance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Id;

@Entity
@Table(name = "leave_type")
public class LeaveType {

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "type")
	private String type;

	public LeaveType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LeaveType(int id, String type) {
		super();
		this.id = id;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
