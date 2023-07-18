package com.example.librarySystem.domain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.app.admin.lendlog.SearchLendLogForm;
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
		lendLog.setReturnDateTime(LocalDateTime.now().minusSeconds(1));
		lendLogRepository.save(lendLog);
	}
	
	public List<LendLog> findAll(){
		return lendLogRepository.findAll();
	}
	
	public List<LendLog> findSearch(SearchLendLogForm searchLendLogForm){
		
		Integer bookId = 0;
		Integer identifyNumber = 0;
		String title = "%";
		String author = "%";
		LocalDateTime fromLoanDateTime = LocalDateTime.of(1,1,1,1,1,1);
		LocalDateTime toLoanDateTime = LocalDateTime.now();
		LocalDateTime fromReturnDate = LocalDateTime.now().minusYears(1);
		LocalDateTime toReturnDate = LocalDateTime.now().plusYears(1);
		
		if(searchLendLogForm.getBooksId() != null) {
			bookId = searchLendLogForm.getBooksId();
		}
		
		if(searchLendLogForm.getIdentifyNumber() != null) {
			identifyNumber = searchLendLogForm.getIdentifyNumber();
		}
		
		if(searchLendLogForm.getTitle() != null) {
			title = "%" + searchLendLogForm.getTitle() + "%";
		}
		
		if(searchLendLogForm.getAuthor() != null) {
			author = "%" + searchLendLogForm.getAuthor() + "%";
		}
		
		if(searchLendLogForm.getFromLoanDate() != null) {
			fromLoanDateTime = searchLendLogForm.getFromLoanDate().atStartOfDay();
		}
		
		if(searchLendLogForm.getToLoanDate() != null) {
			toLoanDateTime = searchLendLogForm.getToLoanDate().atTime(23, 59, 59);
		}
		
		if(searchLendLogForm.getFromReurnDate() != null) {
			fromReturnDate = searchLendLogForm.getFromReurnDate().atStartOfDay();
		}
		
		if(searchLendLogForm.getToReturnDate() != null) {
			toReturnDate = searchLendLogForm.getToReturnDate().atTime(23, 59, 59);
		}
		
		return lendLogRepository.findSearch(bookId, identifyNumber,title, author,
											fromLoanDateTime,toLoanDateTime,fromReturnDate,toReturnDate);
	}
		
}
