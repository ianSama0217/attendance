package com.example.attendance.service.impl;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Employee;
import com.example.attendance.repository.DepartmentsDao;
import com.example.attendance.repository.EmployeeDao;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.BasicRes;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	/* org.slf4j.Logger */
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EmployeeDao empDao;

	@Autowired
	private DepartmentsDao depDao;

	@Override
	public BasicRes create(EmployeeCreateReq req) {
		// �ˬd�����Ƥ����ŭ�
		if (!StringUtils.hasText(req.getId()) || //
				!StringUtils.hasText(req.getDepartment()) || //
				!StringUtils.hasText(req.getName()) || //
				!StringUtils.hasText(req.getPassword()) || //
				!StringUtils.hasText(req.getEmail()) || //
				!StringUtils.hasText(req.getJobPosition()) || //
				req.getBirthDate() == null || //
				req.getArrivalDate() == null) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}

		// �ˬdid�O�_�w�s�b
		if (empDao.existsById(req.getId())) {
			return new BasicRes(RtnCode.ID_HAS_EXISTED);
		}

		// �ˬddepartments_name(deparments) -> �n�T�{�����O�_�s�b
		if (!depDao.existsByName(req.getDepartment())) {
			return new BasicRes(RtnCode.DEPARTMENT_NOT_FOUND);
		}

		// �N�K�X�]���K��
		req.setPassword(encoder.encode(req.getPassword()));
		empDao.save(req);
		req.setPassword("");

		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes login(String id, String password, HttpSession session) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(password)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}

		// �ˬdid & pwd
		Optional<Employee> op = empDao.findById(id);

		if (op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}
		// ���K�X
		Employee employee = op.get();
		// matches(��J���K�X, ��Ʈw�����K�X)
		if (!encoder.matches(password, employee.getPassword())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}
		session.setAttribute(id, id);
		// ���:��, �w�]1800s(30min)
		session.setMaxInactiveInterval(300);
		logger.info("login successful");
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes changePassword(String id, String oldPwd, String newPwd) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(oldPwd) || !StringUtils.hasText(newPwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		
		//�s�K�X���i�����K�X
		if (oldPwd.equals(newPwd)) {
			return new BasicRes(RtnCode.OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENTICAL);
		}
		
		// �ˬdid & pwd
		Optional<Employee> op = empDao.findById(id);

		if (op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}
		// ���K�X
		Employee employee = op.get();
		// matches(��J���K�X, ��Ʈw�����K�X)
		if (!encoder.matches(oldPwd, employee.getPassword())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}
		// �]�w�s�K�X
		employee.setPassword(encoder.encode(newPwd));

		try {
			empDao.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.CHANGE_PASSWORD_ERROR);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}
}
