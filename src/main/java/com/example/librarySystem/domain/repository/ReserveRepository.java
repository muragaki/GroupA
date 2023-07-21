package com.example.librarySystem.domain.repository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.librarySystem.domain.model.Reserve;

/**
 * 
 * ReserveRepositoryクラス
 * 
 * エンティティ型：Reserveクラス
 * 主キー型：Longクラス
 * 
 * @author 3030673
 *
 */
public interface ReserveRepository extends JpaRepository<Reserve, Long> {
	
	/**
	 * ColBooksIdの一致 及び ReserveDateより前の検索
	 * 
	 * @param colBooksId
	 * @param reserveDate
	 * @return List Reserve
	 */
	List<Reserve> findByColBooksIdAndReserveDateBefore(Long colBooksId,LocalDate reserveDate);
	
	/**
	 * colBooksIdが一致し、reserveDateより後のデータをLinkedリストで取得
	 * @param colBooksId
	 * @param reserveDate
	 * @return LinkedList Reserve
	 */
	LinkedList<Reserve> findByColBooksIdAndReserveDateGreaterThanEqualOrderByReserveDateAsc(Long colBooksId,LocalDate reserveDate);
	
	/**
	 * colBooksIdのいずれかでresrveDateより後ろの最も上位のReserveを検索
	 * @param colBooksId
	 * @param reserveDate
	 * @return Reserve
	 */
	Reserve findTopByColBooksIdInAndReserveDateAfterOrderByReserveDateAsc(TreeSet<Long> colBooksId,LocalDate reserveDate);
	
	/**
	 * colBooksIdSetのいずれか & 利用期間がreserveDate内 & reserveId以外 のcolBooksIdを列挙
	 * @param colBooksIdSet
	 * @param reserveDate
	 * @param reserveId
	 * @return List Long
	 */
	@Query("SELECT reserve.colBooksId FROM Reserve reserve "
			+ "WHERE reserve.colBooksId IN ( :colBooksIdSet ) "		//いずれかのcolBooksId
			
			+ "AND ( reserve.reserveDate <= :reserveDate "			//reserveDateとscheduledReturnDateの間
			+ "AND reserve.scheduledReturnDate >= :reserveDate) "
			
			+ "AND reserve.reserveId <> :reserveId")				//reserveId以外
	List<Long> getColbooksIdList(Set<Long> colBooksIdSet,LocalDate reserveDate,Long reserveId);
	
	Reserve findTopByColBooksIdAndReserveDateAfterOrderByReserveDateAsc(Long ColBooksId,LocalDate reserveDate);
	
	Reserve findByReserveIdAndUserId(Long reserveId,String userId);
	
	List<Reserve> findByUserIdOrderByReserveDateAsc(String userId);
	List<Reserve> findByUserIdAndReserveDate(String userId,LocalDate reserveId);
	List<Reserve> findByUserIdAndReserveDateAfter(String userId,LocalDate reserveId);
	
	
}
