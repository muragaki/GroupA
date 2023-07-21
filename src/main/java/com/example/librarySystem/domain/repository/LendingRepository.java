package com.example.librarySystem.domain.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.librarySystem.domain.model.Lending;

/**
 * 
 * LendingRepositoryクラス
 * 
 * エンティティ型：Lendingクラス
 * 主キー型：Longクラス
 * 
 * @author 3030673
 *
 */
public interface LendingRepository extends JpaRepository<Lending, Long> {
	
	/**
	 * userIdの一致検索
	 * @param userId
	 * @return List Lending
	 */
	List<Lending> findByUserId(String userId);
	
	/**
	 * colBooksIdが一致するデータを取得
	 * @param colBooksId
	 * @return
	 */
	Lending findTopByColBooksId(Long colBooksId);
	
	/**
	 * colBooksIdの一致 及び scheduledReturnDate以降
	 * @param colBooksId
	 * @param scheduledReturnDate
	 * @return List Lending
	 * 
	 */
	List<Lending> findByColBooksIdAndScheduledReturnDateGreaterThanEqual(Long colBooksId, LocalDate scheduledReturnDate);
	
	/**
	 * 
	 * colBooksIdを含み scheduledReturnDate以降
	 * @param Set Long : colBooksId
	 * @param scheduledReturnDate
	 * @return List Lending
	 */
	List<Lending> findByColBooksIdInAndScheduledReturnDateGreaterThanEqual(Set<Long> colBooksId , LocalDate scheduledReturnDate);
	
	/**
	 * 
	 * 貸出中検索
	 * 
	 * @param bookId
	 * @param identifyNumber
	 * @param title
	 * @param author
	 * @param fromLoanDateTime
	 * @param toLoanDateTime
	 * @param fromReturnDate
	 * @param toReturnDate
	 * @return List Lending
	 */
	@Query("SELECT lending FROM Lending lending "
			
		 + "WHERE CASE WHEN :bookId = 0 THEN true "								//bookIdの一致検索
		 	+ "ELSE lending.colBooks.booksId = :bookId END "
		 
		 + "AND CASE WHEN :identifyNumber = 0 THEN true "						//identifyNumberの一致検索
		 	+ "ELSE lending.colBooks.identifyNumber = :identifyNumber END "
		 
		 	+ "AND lending.colBooks.books.title LIKE :title "					//titleの一部一致検索
		 
			+ "AND lending.colBooks.books.author LIKE :author "					//authorの一部一致検索
		
		 	+ "AND lending.loanDateTime >= :fromLoanDateTime "					//loanDateTimeの範囲検索
		 	+ "AND lending.loanDateTime <= :toLoanDateTime  "
		 
		 	+ "AND lending.scheduledReturnDate >= :fromReturnDate "				//scheduledReturnDateの範囲検索
			+ "AND lending.scheduledReturnDate <= :toReturnDate ")
	public List<Lending> findSearch(Integer bookId, Integer identifyNumber,String title, String author,
								  LocalDateTime fromLoanDateTime, LocalDateTime toLoanDateTime,
								  LocalDate fromReturnDate, LocalDate toReturnDate);
	
}
