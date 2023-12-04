package com.example.attendance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "job_position")
public class JobPosition {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "title")
	private String title;

	public JobPosition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JobPosition(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
