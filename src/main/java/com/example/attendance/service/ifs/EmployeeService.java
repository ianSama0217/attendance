package com.example.attendance.service.ifs;

import com.example.attendance.vo.EmployeeCreateReq;

import javax.servlet.http.HttpSession;

import com.example.attendance.vo.BasicRes;

public interface EmployeeService {

	public BasicRes create(EmployeeCreateReq req);

	public BasicRes login(String id, String password, HttpSession session);

	public BasicRes changePassword(String id, String oldPwd, String newPwd);

	public BasicRes changePasswordByAuthCode(String id, String authCode, String newPwd);

	public BasicRes forgotPassword(String id, String email);

	// �b���ҥ�
	public BasicRes activate(String executorId, String employeeId);

	// �b������
	public BasicRes deactivate(String executorId, String employeeId);

	// �b���ҥ�+����(�g�b�P�@�Ӥ�k)
	public BasicRes updateActivate(String executorId, String employeeId, boolean isActive);

	// ��¾->�ק窱�A
	public BasicRes resign(String executorId, String employeeId);

	//�w�p�n��¾���u��gresignApplication
	public BasicRes resignApplication(String employeeId);
}
