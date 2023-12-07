package com.example.attendance.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.Employee;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, String> {

	public Employee findByEmail(String email);

	//clearAutomatically = true 清除暫存資料
	//回傳資料型態int => 儲存成功(1筆資料)
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "update Employee set active = inputActive"//
			+ " where id = :inputId")
	public int updateActivate(@Param("inputId") String employeeId, //
			@Param("inputActivate") boolean active);

}
