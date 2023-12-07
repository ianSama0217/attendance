package com.example.attendance.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.AuthCode;
import com.example.attendance.entity.Employee;
import com.example.attendance.entity.ResignApplication;
import com.example.attendance.repository.AuthCodeDao;
import com.example.attendance.repository.DepartmentsDao;
import com.example.attendance.repository.EmployeeDao;
import com.example.attendance.repository.ResignApplicationDao;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.EmployeeCreateReq;

import net.bytebuddy.utility.RandomString;

import com.example.attendance.vo.BasicRes;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	/* org.slf4j.Logger */
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${authCode.expired.time}")
	private int authCodeExpiredTime;

	@Autowired
	private EmployeeDao empDao;

	@Autowired
	private DepartmentsDao depDao;

	@Autowired
	private AuthCodeDao authDao;

	@Autowired
	private ResignApplicationDao resignDao;

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

		// �T�{�b��active���A -> ����
		if (!employee.isActive()) {
			return new BasicRes(RtnCode.ACCOUNT_DEACTIVE);
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

		// �s�K�X���i�����K�X
		if (oldPwd.equals(newPwd)) {
			return new BasicRes(RtnCode.OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENTICAL);
		}

		// ���ΧP�_�O�_���šA����k�������n�J�~��ϥ�
		Employee employee = empDao.findById(id).get();

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

	@Override
	public BasicRes forgotPassword(String id, String email) {
		// id�Memail���S���ŭ�->���~�A �ܤ֭n�O�o�䤤�@��
		if (!StringUtils.hasText(id) && !StringUtils.hasText(email)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}

		Employee employee = null;

		if (StringUtils.hasText(id)) {
			Optional<Employee> op = empDao.findById(id);

			if (op.isEmpty()) {
				return new BasicRes(RtnCode.ID_NOT_FOUND);
			}
			employee = op.get();
		} else {
			employee = empDao.findByEmail(email);
			if (employee == null) {
				return new BasicRes(RtnCode.ID_NOT_FOUND);
			}
		}

		String randomStr = RandomString.make(12);
		// �]�w�s�K�X
		employee.setPassword(encoder.encode(randomStr));

		// �������ҽX
		String authCode = RandomString.make(6);
		// (���ҽX)���Įɶ�30����
		LocalDateTime now = LocalDateTime.now();
		try {
			// �x�semployee���
			empDao.save(employee);
			// �x�sid ���ҽX �{�b�ɶ���Authcode
			authDao.save(new AuthCode(employee.getId(), authCode, now.plusMinutes(authCodeExpiredTime)));

		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.FORGOT_PASSWORD_ERROR);
		}

		// �ɱ� email(��Authcode(���ҽX) �ǤJ email)
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes changePasswordByAuthCode(String id, String authCode, String newPwd) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(authCode) || !StringUtils.hasText(newPwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}

		// �ˬdauthCode
		Optional<AuthCode> op = authDao.findById(id);
		if (op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}

		AuthCode authCodeEntity = op.get();
		// �P�_��J���ҽX�O�_ == Authcode
		if (authCodeEntity.getAuthCode().equals(authCode)) {
			return new BasicRes(RtnCode.AUTH_CODE_NOT_MATCHED);
		}

		// �P�_���ҽX�ɮ�(30����)
		LocalDateTime now = LocalDateTime.now();
		if (now.isAfter(authCodeEntity.getAuthDatetime())) {
			return new BasicRes(RtnCode.AUTH_CODE_EXPIRED);
		}

		Employee employee = empDao.findById(id).get();
		employee.setPassword(encoder.encode(newPwd));

		try {
			empDao.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.CHANGE_PASSWORD_ERROR);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes activate(String executorId, String employeeId) {
		if (!StringUtils.hasText(executorId) || !StringUtils.hasText(employeeId) || //
				executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}

		// ���ΧP�_�O�_���šA����k�������n�J�~��ϥ�
		Employee executor = empDao.findById(executorId).get();
		// department�Oadmin��HR�~��updateActivate
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") || //
				!executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}

		if (empDao.updateActivate(employeeId, true) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes deactivate(String executorId, String employeeId) {
		if (!StringUtils.hasText(executorId) || !StringUtils.hasText(employeeId) || //
				executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}

		// ���ΧP�_�O�_���šA����k�������n�J�~��ϥ�
		Employee executor = empDao.findById(executorId).get();
		// department�Oadmin��HR�~��updateActivate
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") || //
				!executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}

		if (empDao.updateActivate(employeeId, false) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes updateActivate(String executorId, String employeeId, boolean isActive) {
		if (!StringUtils.hasText(executorId) || !StringUtils.hasText(employeeId) || //
				executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}

		// ���ΧP�_�O�_���šA����k�������n�J�~��ϥ�
		Employee executor = empDao.findById(executorId).get();
		// department�Oadmin��HR�~��updateActivate
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") || //
				!executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}

		// �p�G�x�s���\�A�^��1(�x�s��Ʀ��\������)
		if (empDao.updateActivate(employeeId, isActive) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes resign(String executorId, String employeeId) {
		if (!StringUtils.hasText(executorId) || !StringUtils.hasText(employeeId) || //
				executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}

		// ���ΧP�_�O�_���šA����k�������n�J�~��ϥ�
		// �u��HR�i�H�����¾���u���
		Employee executor = empDao.findById(executorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}

		Employee employee = empDao.findById(employeeId).get();
		employee.setResignationDate(LocalDate.now().plusMonths(1));
		employee.setQuitReason("�Ǫ��ۭt��:(");
		try {
			empDao.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes resignApplication(String employeeId) {
		// ���ΧP�_employeeId�O�_���šA����k�������n�J�~��ϥ�
		Employee employee = empDao.findById(employeeId).get();

		try {
			resignDao.save(new ResignApplication(//
					employeeId, employee.getDepartment(), //
					LocalDate.now().plusMonths(1), //
					"�Ǫ��ۭt��:("));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}
}
