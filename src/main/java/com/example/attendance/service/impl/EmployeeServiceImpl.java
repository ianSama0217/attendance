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
		// 檢查必填資料不為空值
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

		// 檢查id是否已存在
		if (empDao.existsById(req.getId())) {
			return new BasicRes(RtnCode.ID_HAS_EXISTED);
		}

		// 檢查departments_name(deparments) -> 要確認部門是否存在
		if (!depDao.existsByName(req.getDepartment())) {
			return new BasicRes(RtnCode.DEPARTMENT_NOT_FOUND);
		}

		// 將密碼設為密文
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

		// 檢查id & pwd
		Optional<Employee> op = empDao.findById(id);

		if (op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}
		// 比對密碼
		Employee employee = op.get();
		// matches(輸入的密碼, 資料庫內的密碼)
		if (!encoder.matches(password, employee.getPassword())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}

		// 確認帳號active狀態 -> 停用
		if (!employee.isActive()) {
			return new BasicRes(RtnCode.ACCOUNT_DEACTIVE);
		}

		session.setAttribute(id, id);
		// 單位:秒, 預設1800s(30min)
		session.setMaxInactiveInterval(300);
		logger.info("login successful");
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes changePassword(String id, String oldPwd, String newPwd) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(oldPwd) || !StringUtils.hasText(newPwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}

		// 新密碼不可等於原密碼
		if (oldPwd.equals(newPwd)) {
			return new BasicRes(RtnCode.OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENTICAL);
		}

		// 不用判斷是否為空，此方法必須先登入才能使用
		Employee employee = empDao.findById(id).get();

		// matches(輸入的密碼, 資料庫內的密碼)
		if (!encoder.matches(oldPwd, employee.getPassword())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}
		// 設定新密碼
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
		// id和email都沒為空值->錯誤， 至少要記得其中一個
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
		// 設定新密碼
		employee.setPassword(encoder.encode(randomStr));

		// 產生驗證碼
		String authCode = RandomString.make(6);
		// (驗證碼)有效時間30分鐘
		LocalDateTime now = LocalDateTime.now();
		try {
			// 儲存employee資料
			empDao.save(employee);
			// 儲存id 驗證碼 現在時間到Authcode
			authDao.save(new AuthCode(employee.getId(), authCode, now.plusMinutes(authCodeExpiredTime)));

		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.FORGOT_PASSWORD_ERROR);
		}

		// 界接 email(把Authcode(驗證碼) 傳入 email)
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes changePasswordByAuthCode(String id, String authCode, String newPwd) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(authCode) || !StringUtils.hasText(newPwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}

		// 檢查authCode
		Optional<AuthCode> op = authDao.findById(id);
		if (op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}

		AuthCode authCodeEntity = op.get();
		// 判斷輸入驗證碼是否 == Authcode
		if (authCodeEntity.getAuthCode().equals(authCode)) {
			return new BasicRes(RtnCode.AUTH_CODE_NOT_MATCHED);
		}

		// 判斷驗證碼時效(30分鐘)
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

		// 不用判斷是否為空，此方法必須先登入才能使用
		Employee executor = empDao.findById(executorId).get();
		// department是admin或HR才能updateActivate
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

		// 不用判斷是否為空，此方法必須先登入才能使用
		Employee executor = empDao.findById(executorId).get();
		// department是admin或HR才能updateActivate
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

		// 不用判斷是否為空，此方法必須先登入才能使用
		Employee executor = empDao.findById(executorId).get();
		// department是admin或HR才能updateActivate
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") || //
				!executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}

		// 如果儲存成功，回傳1(儲存資料成功的筆數)
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

		// 不用判斷是否為空，此方法必須先登入才能使用
		// 只有HR可以更改離職員工資料
		Employee executor = empDao.findById(executorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}

		Employee employee = empDao.findById(employeeId).get();
		employee.setResignationDate(LocalDate.now().plusMonths(1));
		employee.setQuitReason("學長欺負我:(");
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
		// 不用判斷employeeId是否為空，此方法必須先登入才能使用
		Employee employee = empDao.findById(employeeId).get();

		try {
			resignDao.save(new ResignApplication(//
					employeeId, employee.getDepartment(), //
					LocalDate.now().plusMonths(1), //
					"學長欺負我:("));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}
}
