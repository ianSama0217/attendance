package com.example.attendance.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mysql.cj.jdbc.Blob;

@Entity
@Table(name = "leave_application")
public class LeaveApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "serial_no")
	private int serialNo;

	@Column(name = "application_no")
	private String applicationNo;

	@Column(name = "employee_id")
	private String employeeId;

	@Column(name = "employee_department")
	private String employeeDepartment;

	@Column(name = "leave_type")
	private String leaveType;

	@Column(name = "leave_start_datetime")
	private LocalDateTime leaveStartDatetime;

	@Column(name = "leave_end_datetime")
	private LocalDateTime leaveEndDatetime;

	@Column(name = "total_hours")
	private int totalHours;

	@Column(name = "leave_reason")
	private String leaveReason;

	@Column(name = "review_id")
	private String reviewId;

	@Column(name = "review_datetime")
	private LocalDateTime reviewDatetime;

	@Column(name = "review_status")
	private String reviewStatus;

	@Column(name = "reject_reason")
	private String rejectReason;

	@Column(name = "applied_datetime")
	private LocalDateTime appliedDatetime;

	@Column(name = "update_datetime")
	private LocalDateTime updateDatetime;

	@Column(name = "certification")
	private byte[] certification;

	public LeaveApplication() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LeaveApplication(String applicationNo, String employeeId, String employeeDepartment, String leaveType,
			LocalDateTime leaveStartDatetime, LocalDateTime leaveEndDatetime, int totalHours, String leaveReason,
			String reviewId, LocalDateTime reviewDatetime, String reviewStatus, String rejectReason,
			LocalDateTime appliedDatetime, LocalDateTime updateDatetime) {
		super();
		this.applicationNo = applicationNo;
		this.employeeId = employeeId;
		this.employeeDepartment = employeeDepartment;
		this.leaveType = leaveType;
		this.leaveStartDatetime = leaveStartDatetime;
		this.leaveEndDatetime = leaveEndDatetime;
		this.totalHours = totalHours;
		this.leaveReason = leaveReason;
		this.reviewId = reviewId;
		this.reviewDatetime = reviewDatetime;
		this.reviewStatus = reviewStatus;
		this.rejectReason = rejectReason;
		this.appliedDatetime = appliedDatetime;
		this.updateDatetime = updateDatetime;
	}

	public LeaveApplication(LeaveApplication application) {
		super();
		this.applicationNo = application.getApplicationNo();
		this.employeeId = application.getEmployeeId();
		this.employeeDepartment = application.getEmployeeDepartment();
		this.leaveType = application.getEmployeeDepartment();
		this.leaveStartDatetime = application.getLeaveStartDatetime();
		this.leaveEndDatetime = application.getLeaveEndDatetime();
		this.totalHours = application.getTotalHours();
		this.leaveReason = application.getLeaveReason();
		this.reviewId = application.getReviewId();
		this.reviewDatetime = application.getReviewDatetime();
		this.reviewStatus = application.getReviewStatus();
		this.rejectReason = application.getRejectReason();
		this.appliedDatetime = application.getAppliedDatetime();
		this.updateDatetime = application.getUpdateDatetime();
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeDepartment() {
		return employeeDepartment;
	}

	public void setEmployeeDepartment(String employeeDepartment) {
		this.employeeDepartment = employeeDepartment;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public LocalDateTime getLeaveStartDatetime() {
		return leaveStartDatetime;
	}

	public void setLeaveStartDatetime(LocalDateTime leaveStartDatetime) {
		this.leaveStartDatetime = leaveStartDatetime;
	}

	public LocalDateTime getLeaveEndDatetime() {
		return leaveEndDatetime;
	}

	public void setLeaveEndDatetime(LocalDateTime leaveEndDatetime) {
		this.leaveEndDatetime = leaveEndDatetime;
	}

	public int getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(int totalHours) {
		this.totalHours = totalHours;
	}

	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	public String getReviewId() {
		return reviewId;
	}

	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}

	public LocalDateTime getReviewDatetime() {
		return reviewDatetime;
	}

	public void setReviewDatetime(LocalDateTime reviewDatetime) {
		this.reviewDatetime = reviewDatetime;
	}

	public String getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public LocalDateTime getAppliedDatetime() {
		return appliedDatetime;
	}

	public void setAppliedDatetime(LocalDateTime appliedDatetime) {
		this.appliedDatetime = appliedDatetime;
	}

	public LocalDateTime getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(LocalDateTime updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public byte[] getCertification() {
		return certification;
	}

	public void setCertification(byte[] certification) {
		this.certification = certification;
	}

}
