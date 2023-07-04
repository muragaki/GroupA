package com.example.librarySystem.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.repository.LendingRepository;

@Service
public class LendingService {
	
	@Autowired
	LendingRepository lendingRepository;
	
	public List<Lending> findUserList(String userId){
		return lendingRepository.findByUserId(userId);
	}
}