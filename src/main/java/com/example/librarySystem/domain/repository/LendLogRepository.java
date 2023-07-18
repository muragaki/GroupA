package com.example.librarySystem.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.librarySystem.domain.model.LendLog;

public interface LendLogRepository extends JpaRepository<LendLog, Long> {
	
	List<LendLog> findByUserId(String userId);
	
	@Query("SELECT lendlog FROM LendLog lendlog "
			
		 + "WHERE CASE WHEN :bookId = 0 THEN true "
		 	+ "ELSE lendlog.colBooks.booksId = :bookId END "
		 
		 + "AND CASE WHEN :identifyNumber = 0 THEN true "
		 	+ "ELSE lendlog.colBooks.identifyNumber = :identifyNumber END "
		 
		 	+ "AND lendlog.colBooks.books.title LIKE :title "
		 
			+ "AND lendlog.colBooks.books.author LIKE :author " 
		
		 	+ "AND lendlog.loanDateTime >= :fromLoanDateTime "
		 
		 	+ "AND lendlog.loanDateTime <= :toLoanDateTime  "
		 
		 	+ "AND lendlog.returnDateTime >= :fromReturnDateTime "
		 
			+ "AND lendlog.returnDateTime <= :toReturnDateTime ")
	public List<LendLog> findSearch(Integer bookId, Integer identifyNumber,String title, String author,
								  LocalDateTime fromLoanDateTime, LocalDateTime toLoanDateTime,
								  LocalDateTime fromReturnDateTime, LocalDateTime toReturnDateTime);

}
