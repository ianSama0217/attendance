package com.example.attendance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Employee;
import com.example.attendance.repository.DepartmentsDao;
import com.example.attendance.repository.EmployeeDao;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.EmployeeCreateRes;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	private EmployeeDao empDao;

	@Autowired
	private DepartmentsDao depDao;

	@Override
	public EmployeeCreateRes create(Employee req) {
		// �ˬd�����Ƥ����ŭ�
		if (!StringUtils.hasText(req.getId()) || //
				!StringUtils.hasText(req.getDepartment()) || //
				!StringUtils.hasText(req.getName()) || //
				!StringUtils.hasText(req.getPassword()) || //
				!StringUtils.hasText(req.getEmail()) || //
				!StringUtils.hasText(req.getJobPosition()) || //
				req.getBirthDate() == null || //
				req.getArrivalDate() == null) {
			return new EmployeeCreateRes(RtnCode.PARAM_ERROR);
		}

		// �ˬdid�O�_�w�s�b
		if (empDao.existsById(req.getId())) {
			return new EmployeeCreateRes(RtnCode.ID_HAS_EXISTED);
		}

		// �ˬddepartments_name(deparments) -> �n�T�{�����O�_�s�b
		if (!depDao.existsByName(req.getDepartment())) {
			return new EmployeeCreateRes(RtnCode.DEPARTMENT_NOT_FOUND);
		}

		// �N�K�X�]���K��
		req.setPassword(encoder.encode(req.getPassword()));
		empDao.save(req);
		req.setPassword("");

		return new EmployeeCreateRes(RtnCode.SUCCESSFUL);
	}
}
