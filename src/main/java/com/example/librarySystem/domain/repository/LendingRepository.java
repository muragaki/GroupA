package com.example.librarySystem.domain.repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.librarySystem.domain.model.Lending;

public interface LendingRepository extends JpaRepository<Lending, Long> {
	
	List<Lending> findByUserId(String userId);
	
	List<Lending> findByColBooksIdAndScheduledReturnDateGreaterThanEqual(Long colBooskId, LocalDate scheduledReturnDate);
	
	List<Lending> findByColBooksIdInAndScheduledReturnDateGreaterThanEqual(Set<Long> colBooksId , LocalDate scheduledReturnDate);
	
	
	@Query("SELECT lending FROM Lending lending "
		 + "WHERE CASE WHEN :bookId IS NULL THEN true "
		 	+ "ELSE lending.colBooks.booksId = :bookId END "
		 + "AND CASE WHEN :identifyNumber IS NULL THEN true "
		 	+ "ELSE lending.colBooks.identifyNumber = :identifyNumber END "
		 + "AND CASE WHEN :title IS NULL THEN true "
			+ "ELSE lending.colBooks.books.title LIKE %:title% END "
		 + "AND CASE WHEN :author IS NULL THEN true "
			+ "ELSE lending.colBooks.books.author LIKE %:author% END " 
		 + "AND CASE WHEN :fromLoanDateTime IS NULL THEN true END "
		 /*	+ "ELSE lending.loanDateTime >= :fromLoanDateTime END "*/
		 + "AND CASE WHEN :toLoanDateTime IS NULL THEN true END "
		 /*	+ "ELSE lending.loanDateTime <= :toLoanDateTime END "*/
		 + "AND CASE WHEN :fromReturnDate IS NULL THEN true "
		 	+ "ELSE lending.scheduledReturnDate >= :fromReturnDate END "
		 + "AND CASE WHEN :toReturnDate IS NULL THEN true "
		 	+ "ELSE lending.scheduledReturnDate <= :toReturnDate END ")
	public List<Lending> findSearch(Integer bookId, Integer identifyNumber,String title, String author,
								  Timestamp fromLoanDateTime, Timestamp toLoanDateTime,
								  LocalDate fromReturnDate, LocalDate toReturnDate);
	
	@Query("SELECT lending FROM Lending lending "
			 + "WHERE CASE WHEN :fromLoanDateTime IS NULL THEN true "
			 	+ "ELSE lending.loanDateTime >= :fromLoanDateTime END "
			 + "AND CASE WHEN :toLoanDateTime IS NULL THEN true "
			 	+ "ELSE lending.loanDateTime <= :toLoanDateTime END ")
		public List<Lending> findSearch(Date fromLoanDateTime, Date toLoanDateTime);
		
	
}
