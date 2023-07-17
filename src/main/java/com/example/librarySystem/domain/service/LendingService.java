package com.example.librarySystem.domain.service;

import java.util.Date;
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
		
		
		Date fromLoanDateTime = null;
		Date toLoanDateTime = null;
		
		/*
		if(searchLendingForm.getFromLoanDate() != null) {
			fromLoanDateTime = Timestamp.valueOf(searchLendingForm.getFromLoanDate().atStartOfDay()).toString();
		}
		if(searchLendingForm.getToLoanDate() != null) {
			//toLoanDateTime = Date.from(ZonedDateTime.of(searchLendingForm.getToLoanDate().atTime(23, 59, 59),ZoneId.systemDefault()).toInstant());
			toLoanDateTime = Timestamp.valueOf(searchLendingForm.getToLoanDate().atTime(23, 59, 59)).toString();
		}
		*/
		return lendingRepository.findSearch(fromLoanDateTime, toLoanDateTime);
		
		/*
		return lendingRepository.findSearch(searchLendingForm.getBooksId(), searchLendingForm.getIdentifyNumber(),
											searchLendingForm.getTitle(), searchLendingForm.getAuthor(),
											fromLoanDateTime,toLoanDateTime,
											searchLendingForm.getFromLoanDate(),searchLendingForm.getToReturnDate());
		
		*/
		
	}
}
