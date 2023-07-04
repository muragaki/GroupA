package com.example.librarySystem.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.LendLog;
import com.example.librarySystem.domain.repository.LendLogRepository;

@Service
public class LendLogService {

	@Autowired
	LendLogRepository lendLogRepository;
	
	public List<LendLog> findUserList(String userId){
		return lendLogRepository.findByUserId(userId);
	}
	
}
