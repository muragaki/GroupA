package com.example.librarySystem.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.app.admin.lending.SearchLendingForm;
import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.repository.LendingRepository;

@Service
public class LendingService {
	
	@Autowired
	LendingRepository lendingRepository;
	
	public List<Lending> findUserList(String userId){
		return lendingRepository.findByUserId(userId);
	}
	
	public Lending saveLend(Lending lending) {
		return lendingRepository.save(lending);
	}
	
	public void deleteLend(Long lendingId) {
		lendingRepository.deleteById(lendingId);
	}
	
	public Lending readByLendingId(Long lendingId) {
		return lendingRepository.findById(lendingId).get();
	}
	
	public List<Lending> findAll(){
		return lendingRepository.findAll();
	}
	
	public List<Lending> findSearch(SearchLendingForm searchLendingForm){
		
		if(searchLendingForm.getFromLoanDate()==null) {
			searchLendingForm.setFromLoanDate(LocalDate.of(1,1,1));
		}
		if(searchLendingForm.getToLoanDate()==null) {
			searchLendingForm.setToLoanDate(LocalDate.now());
		}
		
		LocalDateTime fromLoanDateTime = searchLendingForm.getFromLoanDate().atStartOfDay();
		LocalDateTime toLoanDateTime =searchLendingForm.getToLoanDate().atTime(23, 59, 59);
		
		return null;
		
	}
}
