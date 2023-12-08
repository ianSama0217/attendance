package com.example.attendance.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.checkerframework.checker.i18nformatter.qual.I18nFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.attendance.constants.LeaveType;
import com.example.attendance.constants.ReviewType;
import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Employee;
import com.example.attendance.entity.LeaveApplication;
import com.example.attendance.repository.EmployeeDao;
import com.example.attendance.repository.LeaveApplicationDao;
import com.example.attendance.service.ifs.LeaveApplicationService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.LeaveApplicationReq;

@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

	/* org.slf4j.Logger */
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EmployeeDao empDao;

	@Autowired
	private LeaveApplicationDao leaveDao;

	@Override
	public BasicRes apply(LeaveApplicationReq req) {
		if (LeaveType.parser(req.getEmployeeDepartment()) == null) {
			return new BasicRes(RtnCode.LEAVE_TYPE_ERROR);
		}

		// �}�l�ɶ����i�H�j�󵲧��ɶ�
		if (req.getLeaveStartDatetime().isAfter(req.getLeaveEndDatetime())) {
			return new BasicRes(RtnCode.LEAVE_APPLIED_DATETIME_ERROR);
		}

		if (StringUtils.hasText(req.getLeaveReason())) {
			return new BasicRes(RtnCode.LEAVE_REASON_CANNOT_BE_EMPTY);
		}

		if (StringUtils.hasText(req.getReviewId())) {
			return new BasicRes(RtnCode.LEAVE_REVIEW_ID_CANNOT_BE_EMPTY);
		}

		Optional<Employee> op = empDao.findById(req.getReviewId());
		if (op.isEmpty()) {
			return new BasicRes(RtnCode.LEAVE_REVIEW_ID_NOT_FOUND);
		}

		Employee reviwer = op.get();
		/* TODO �v�������n�ϥ�jobPosition����@��k�P�_ */
		if (false) {
			return new BasicRes(RtnCode.PERMISSION_DENIED);
		}

		// ���Ͱ���s��(applicationNo)
		// TODO
		LocalDateTime now = LocalDateTime.now();

		// �]�w��s�ɶ����x�s
		req.setUpdateDatetime(LocalDateTime.now());
		try {
			LeaveApplicationReq res = leaveDao.save(req);
			// TODO �x�s������H�eemail:�H�󤺮e�n������y����(id)
			int serialNo = res.getSerialNo();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.LEAVE_APPLIED_ERROR);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes review(String reviwerId, String applicationNo) {
		// ���ΧP�_reviwerId�O�_���šALOGIN����~��ϥ�
		if (StringUtils.hasText(applicationNo)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}

		List<LeaveApplication> list = leaveDao.findByApplicationNo(applicationNo);
		// �P�_�O�_������
		if (CollectionUtils.isEmpty(list)) {
			return new BasicRes(RtnCode.LEAVE_APPLICATION_NOT_FOUND);
		}

		LeaveApplication application = list.get(list.size() - 1);
		Employee reviewer = empDao.findById(reviwerId).get();
		// ����ӽЪ̥����M�f�̡֪A�P����(���D��)
		if (!application.getEmployeeDepartment().equalsIgnoreCase(reviewer.getDepartment())) {
			return new BasicRes(RtnCode.PERMISSION_DENIED);
		}

		/* TODO �v�������n�ϥ�jobPosition����@��k�P�_ */
		if (false) {
			return new BasicRes(RtnCode.PERMISSION_DENIED);
		}

		// �]�wReviewDatetime�BReviewStatus
		LocalDateTime now = LocalDateTime.now();
		application.setReviewDatetime(now);
		application.setReviewStatus(ReviewType.PASS.getType());

		try {
			leaveDao.save(new LeaveApplication(application));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.LEAVE_APPLIED_ERROR);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}

}
