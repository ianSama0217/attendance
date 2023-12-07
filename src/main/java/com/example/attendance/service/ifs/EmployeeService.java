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

	// 帳號啟用
	public BasicRes activate(String executorId, String employeeId);

	// 帳號停用
	public BasicRes deactivate(String executorId, String employeeId);

	// 帳號啟用+停用(寫在同一個方法)
	public BasicRes updateActivate(String executorId, String employeeId, boolean isActive);

	// 離職->修改狀態
	public BasicRes resign(String executorId, String employeeId);

	//預計要離職員工填寫resignApplication
	public BasicRes resignApplication(String employeeId);
}
