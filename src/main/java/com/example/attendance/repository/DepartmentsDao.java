package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.Departments;

@Repository
public interface DepartmentsDao extends JpaRepository<Departments, String> {

	public boolean existsByIdIn(List<String> idList);

	public boolean existsByName(String name);
}
