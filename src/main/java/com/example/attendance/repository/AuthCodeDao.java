package com.example.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.AuthCode;

@Repository
public interface AuthCodeDao extends JpaRepository<AuthCode, String>{

}
