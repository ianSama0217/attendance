package com.example.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.LeaveType;

@Repository
public interface LeaveTypeDao extends JpaRepository<LeaveType, Integer>{

}
