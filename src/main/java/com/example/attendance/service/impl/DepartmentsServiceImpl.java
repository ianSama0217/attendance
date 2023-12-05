package com.example.attendance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.service.ifs.DepartmentsService;
import com.example.attendance.vo.DepartmentsCreateReq;
import com.example.attendance.vo.EmployeeCreateRes;

@Service
public class DepartmentsServiceImpl implements DepartmentsService {

	@Autowired
	private DepartmentsCreateReq dao;

	@Override
	public EmployeeCreateRes create(DepartmentsCreateReq req) {
		// �ˬd�@��List��map�O�_���� CollectionUtils
		if (CollectionUtils.isEmpty(req.getDepartments())) {
			return new EmployeeCreateRes(RtnCode.PARAM_ERROR);
		}
		return new EmployeeCreateRes(RtnCode.SUCCESSFUL);
	}

}
