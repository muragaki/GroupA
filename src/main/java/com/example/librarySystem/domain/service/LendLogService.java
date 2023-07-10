package com.example.librarySystem.domain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.LendLog;
import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.repository.LendLogRepository;

@Service
public class LendLogService {

	@Autowired
	LendLogRepository lendLogRepository;
	
	public List<LendLog> findUserList(String userId){
		return lendLogRepository.findByUserId(userId);
	}
	
	public void saveLendingNow(Lending lending) {
		LendLog lendLog = new LendLog(lending);
		lendLog.setReturnDateTime(LocalDateTime.now());
		lendLogRepository.save(lendLog);
	}
	public List<LendLog> findAll(){
		return lendLogRepository.findAll();
	}
}
