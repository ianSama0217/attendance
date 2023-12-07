package com.example.attendance.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "resign_application")
public class ResignApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "employee_id")
	private String employeeId;

	@Column(name = "department")
	private String department;

	@Column(name = "resignation_date")
	private LocalDate resignationDate;

	@Column(name = "quit_reason")
	private String quitReason;

	@Column(name = "director_review")
	private boolean directorReview;

	@Column(name = "hr_review")
	private boolean hrReview;

	public ResignApplication() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResignApplication(String employeeId, String department, LocalDate resignationDate, String quitReason) {
		super();
		this.employeeId = employeeId;
		this.department = department;
		this.resignationDate = resignationDate;
		this.quitReason = quitReason;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public LocalDate getResignationDate() {
		return resignationDate;
	}

	public void setResignationDate(LocalDate resignationDate) {
		this.resignationDate = resignationDate;
	}

	public String getQuitReason() {
		return quitReason;
	}

	public void setQuitReason(String quitReason) {
		this.quitReason = quitReason;
	}

	public boolean isDirectorReview() {
		return directorReview;
	}

	public void setDirectorReview(boolean directorReview) {
		this.directorReview = directorReview;
	}

	public boolean isHrReview() {
		return hrReview;
	}

	public void setHrReview(boolean hrReview) {
		this.hrReview = hrReview;
	}
}
