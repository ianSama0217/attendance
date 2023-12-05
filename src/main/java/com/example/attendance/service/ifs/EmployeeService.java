package com.example.attendance.service.ifs;

import com.example.attendance.entity.Employee;
import com.example.attendance.vo.EmployeeCreateRes;

public interface EmployeeService {

	public EmployeeCreateRes create(Employee req);
}
