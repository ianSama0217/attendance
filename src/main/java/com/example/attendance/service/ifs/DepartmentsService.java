package com.example.attendance.service.ifs;

import com.example.attendance.vo.DepartmentsCreateReq;
import com.example.attendance.vo.EmployeeCreateRes;

public interface DepartmentsService {

	public EmployeeCreateRes create(DepartmentsCreateReq req);
}
