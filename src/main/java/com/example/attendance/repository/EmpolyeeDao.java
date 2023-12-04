package com.example.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.Empolyee;

@Repository
public interface EmpolyeeDao extends JpaRepository<Empolyee, String> {

}
