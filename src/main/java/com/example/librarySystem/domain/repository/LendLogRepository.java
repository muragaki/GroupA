package com.example.librarySystem.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarySystem.domain.model.LendLog;

public interface LendLogRepository extends JpaRepository<LendLog, Long> {
	
	List<LendLog> findByUserId(String userId);

}
