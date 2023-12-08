package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.LeaveApplication;

@Repository
public interface LeaveApplicationDao extends JpaRepository<LeaveApplication, Integer> {

	public List<LeaveApplication> findByApplicationNo(String applicationNo);
}
