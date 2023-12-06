package com.example.attendance;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.attendance.entity.Employee;
import com.example.attendance.repository.EmployeeDao;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.BasicRes;

@SpringBootTest
public class EmployeeTest {

	@Autowired
	private EmployeeService service;

	@Autowired
	private EmployeeDao dao;
	@Test
	public void createDaoTest() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		Employee emp = new Employee("789", "ADMIN", "OXO", encoder.encode("a12345"), "email", "taipei",
				LocalDate.of(2000, 2, 17), LocalDate.of(2023, 12, 1));

		Employee res = dao.save(emp);
		System.out.println("id: " + res.getId());
	}
}
