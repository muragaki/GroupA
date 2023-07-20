package com.example.librarySystem.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.librarySystem.domain.model.LendLog;

/**
 * 
 * LendLogRepositoryクラス
 * 
 * エンティティ型：LendLonクラス
 * 主キー型：Longクラス
 * 
 * @author 3030673
 *
 */
public interface LendLogRepository extends JpaRepository<LendLog, Long> {
	
	/**
	 * userIdの一致検索
	 * 
	 * @param userId
	 * @return List LendLog
	 */
	List<LendLog> findByUserId(String userId);
	
	/**
	 * 
	 * 利用履歴条件検索
	 * 
	 * @param bookId
	 * @param identifyNumber
	 * @param title
	 * @param author
	 * @param fromLoanDateTime
	 * @param toLoanDateTime
	 * @param fromReturnDateTime
	 * @param toReturnDateTime
	 * @return List LendLog
	 * 
	 */
	@Query("SELECT lendlog FROM LendLog lendlog "
			
		 + "WHERE CASE WHEN :bookId = 0 THEN true "								//bookIdの一致検索
		 	+ "ELSE lendlog.colBooks.booksId = :bookId END "
		 
		 + "AND CASE WHEN :identifyNumber = 0 THEN true "						//identifyNumberの一致検索
		 	+ "ELSE lendlog.colBooks.identifyNumber = :identifyNumber END "
		 
		 	+ "AND lendlog.colBooks.books.title LIKE :title "					//titleの一部一致検索
		 
			+ "AND lendlog.colBooks.books.author LIKE :author " 				//authorの一部一致検索
		
		 	+ "AND lendlog.loanDateTime >= :fromLoanDateTime "					//loanDateTimeの範囲検索
		 	+ "AND lendlog.loanDateTime <= :toLoanDateTime "
		 
		 	+ "AND lendlog.returnDateTime >= :fromReturnDateTime "				//returnDateTimeの範囲検索
			+ "AND lendlog.returnDateTime <= :toReturnDateTime ")
	public List<LendLog> findSearch(Integer bookId, Integer identifyNumber,String title, String author,
								  LocalDateTime fromLoanDateTime, LocalDateTime toLoanDateTime,
								  LocalDateTime fromReturnDateTime, LocalDateTime toReturnDateTime);

}
