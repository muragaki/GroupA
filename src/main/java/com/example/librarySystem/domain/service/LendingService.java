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
		
		Integer bookId = 0;
		Integer identifyNumber = 0;
		String title = "%";
		String author = "%";
		LocalDateTime fromLoanDateTime = LocalDateTime.of(1,1,1,1,1,1);
		LocalDateTime toLoanDateTime = LocalDateTime.now();
		LocalDate fromReturnDate = LocalDate.now().minusYears(1);
		LocalDate toReturnDate = LocalDate.now().plusYears(1);
		
		if(searchLendingForm.getBooksId() != null) {
			bookId = searchLendingForm.getBooksId();
		}
		
		if(searchLendingForm.getIdentifyNumber() != null) {
			identifyNumber = searchLendingForm.getIdentifyNumber();
		}
		
		if(searchLendingForm.getTitle() != null) {
			title = "%" + searchLendingForm.getTitle() + "%";
		}
		
		if(searchLendingForm.getAuthor() != null) {
			author = "%" + searchLendingForm.getAuthor() + "%";
		}
		
		if(searchLendingForm.getFromLoanDate() != null) {
			fromLoanDateTime = searchLendingForm.getFromLoanDate().atStartOfDay();
		}
		
		if(searchLendingForm.getToLoanDate() != null) {
			toLoanDateTime = searchLendingForm.getToLoanDate().atTime(23, 59, 59);
		}
		
		if(searchLendingForm.getFromReurnDate() != null) {
			fromReturnDate = searchLendingForm.getFromReurnDate();
		}
		
		if(searchLendingForm.getToReturnDate() != null) {
			toReturnDate = searchLendingForm.getToReturnDate();
		}
		
		return lendingRepository.findSearch(bookId, identifyNumber,title, author,
											fromLoanDateTime,toLoanDateTime,fromReturnDate,toReturnDate);
		
		
		
	}
}
