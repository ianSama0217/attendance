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
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.LoginReq;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService service;

	@PostMapping(value = "/attendance/login")
	public BasicRes login(@RequestBody LoginReq req, HttpSession session) {
		// �p�Gid == null -> �n�D�n�J�b��
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
		// �p�Gid == null -> �n�D�n�J�b��
		if (session.getAttribute(id) == null) {
			return service.login(id, pwd, session);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@GetMapping(value = "/attendance/logout")
	public BasicRes logout(HttpSession session) {
		// ��session����
		session.invalidate();
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@PostMapping(value = "/attendance/employee/create")
	public BasicRes create(@RequestBody EmployeeCreateReq req, HttpSession session) {
		// ���n�J�b���~��s�W
		if (session.getAttribute(req.getCreatorId()) == null) {
			return new BasicRes(RtnCode.LOGIN_FIRST);
		}
		// �v�������OADMIN�~��s�W
		if (session.getAttribute(req.getCreatorId()).toString().equalsIgnoreCase("admin")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}

		return service.create(req);
	}
}
