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
		// 檢查必填資料不為空值
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

		// 檢查id是否已存在
		if (empDao.existsById(req.getId())) {
			return new EmployeeCreateRes(RtnCode.ID_HAS_EXISTED);
		}

		// 檢查departments_name(deparments) -> 要確認部門是否存在
		if (!depDao.existsByName(req.getDepartment())) {
			return new EmployeeCreateRes(RtnCode.DEPARTMENT_NOT_FOUND);
		}

		// 將密碼設為密文
		req.setPassword(encoder.encode(req.getPassword()));
		empDao.save(req);
		req.setPassword("");

		return new EmployeeCreateRes(RtnCode.SUCCESSFUL);
	}
}
