package com.example.attendance.service.ifs;

import com.example.attendance.vo.DepartmentsCreateReq;
import com.example.attendance.vo.DepartmentsCreateRes;

public interface DepartmentsService {

	public DepartmentsCreateRes create(DepartmentsCreateReq req);
}
