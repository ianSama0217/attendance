package com.example.attendance.service.ifs;

import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.LeaveApplicationReq;

public interface LeaveApplicationService {
	public BasicRes apply(LeaveApplicationReq req);

	public BasicRes review(String reviwerId, String applicationNo);
}
