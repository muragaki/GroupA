package com.example.librarySystem.domain.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.librarySystem.domain.model.Books;
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
		 + "AND lending.loanDateTime BETWEEN :fromLoanDateTime AND :toLoanDateTime "
		 + "AND lending.scheduledReturnDate BETWEEN :fromReturnDate AND :toReturnDate ")
	public List<Books> findSearch(Integer bookId, Integer identifyNumber,String title, String author,
								  LocalDateTime fromLoanDateTime, LocalDateTime toLoanDateTime,
								  LocalDate fromReturnDate, LocalDate toReturnDate);
			
}
