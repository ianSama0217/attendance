package com.example.attendance.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.ChangePasswordReq;
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.ForgotPasswordReq;
import com.example.attendance.vo.LoginReq;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService service;

	@PostMapping(value = "/attendance/login")
	public BasicRes login(@RequestBody LoginReq req, HttpSession session) {
		// 如果id == null -> 要求登入帳號
		if (session.getAttribute(req.getId()) == null) {
			return service.login(req.getId(), req.getPwd(), session);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@GetMapping(value = "/attendance/loginParam")
	public BasicRes loginParam(//
			@RequestParam(value = "id") String id, //
			@RequestParam(value = "password") String pwd, //
			HttpSession session) {
		// 如果id == null -> 要求登入帳號
		if (session.getAttribute(id) == null) {
			return service.login(id, pwd, session);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@GetMapping(value = "/attendance/logout")
	public BasicRes logout(HttpSession session) {
		// 讓session失效
		session.invalidate();
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@PostMapping(value = "/attendance/employee/create")
	public BasicRes create(@RequestBody EmployeeCreateReq req, HttpSession session) {
		// 有登入帳號才能新增
		if (session.getAttribute(req.getCreatorId()) == null) {
			return new BasicRes(RtnCode.LOGIN_FIRST);
		}
		// 權限必須是ADMIN才能新增
		if (session.getAttribute(req.getCreatorId()).toString().equalsIgnoreCase("admin")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}

		return service.create(req);
	}

	@PostMapping(value = "/attendance/employee/change_password")
	public BasicRes changePassword(@RequestBody ChangePasswordReq req, HttpSession session) {
		// 有登入帳號才能更改密碼
		if (session.getAttribute(req.getId()) == null) {
			return new BasicRes(RtnCode.LOGIN_FIRST);
		}

		return service.changePassword(req.getId(), req.getOldPwd(), req.getNewPwd());
	}

	@PostMapping(value = "/attendance/employee/forgot_password")
	public BasicRes forgotPassword(@RequestBody ForgotPasswordReq req) {
		return service.forgotPassword(req.getId(), req.getEmail());
	}

	@PostMapping(value = "/attendance/employee/change_password_by_auth_code")
	public BasicRes changePasswordByAuthCode(@RequestBody ChangePasswordReq req) {
		return service.changePasswordByAuthCode(req.getId(), req.getAuthCode(), req.getNewPwd());
	}
}
