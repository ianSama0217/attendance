package com.example.attendance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.attendance.repository.EmployeeDao;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.EmployeeCreateReq;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDao dao;

	@Override
	public void create(EmployeeCreateReq req) {
		if(StringUtils.hasText(req.getId())|| // 
				StringUtils.hasText(req.getName())||//
				StringUtils.hasText(req.getId())||//
				StringUtils.hasText(req.getId())||//
				StringUtils.hasText(req.getId())) {
			
		}

	}

}
