package com.example.attendance;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.attendance.entity.Employee;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.EmployeeCreateRes;

@SpringBootTest
public class EmployeeTest {

	@Autowired
	private EmployeeService service;

	@Test
	public void createTest() {
		Employee emp = new Employee("123", "ADMIN", "ian", "a1234", "email", "taipei", LocalDate.of(2000, 2, 17),
				LocalDate.of(2023, 12, 1), null, null, true, 0, 0);

		EmployeeCreateRes res = service.create(emp);
		System.out.println(res.getRtnCode().getMessage());
	}
}
