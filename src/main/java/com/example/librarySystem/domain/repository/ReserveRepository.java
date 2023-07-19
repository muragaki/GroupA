package com.example.librarySystem.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.librarySystem.domain.model.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
	
	List<Reserve> findByColBooksIdAndReserveDateBefore(Long colBooksId,LocalDate reserveDate);
	
	@Query("SELECT reserve FROM Reserve reserve "
			+ "WHERE reserve.colBooksId = :colBooksId "
			+ "AND (( reserve.reserveDate <= :reserveDate "
			+ "AND reserve.scheduledReturnDate >= :reserveDate) "
			+ "OR ( reserve.reserveDate <= :scheduledReturnDate "
			+ "AND reserve.reserveDate >= :scheduledReturnDate)) "
			+ "ORDER BY reserve.colBooksId")
	List<Reserve> checkReserveSpace(Long colBooksId,LocalDate reserveDate,LocalDate scheduledReturnDate);
	
	Reserve findTopByColBooksIdInAndReserveDateAfterOrderByReserveDateAsc(TreeSet<Long> colBooksId,LocalDate reserveDate);
	
	@Query("SELECT reserve.colBooksId FROM Reserve reserve "
			+ "WHERE reserve.colBooksId IN ( :colBooksIdSet ) "
			+ "AND ( reserve.reserveDate <= :reserveDate "
			+ "AND reserve.scheduledReturnDate >= :reserveDate) "
			+ "AND reserve.reserveId <> :reserveId")
	List<Long> getColbooksIdList(Set<Long> colBooksIdSet,LocalDate reserveDate,Long reserveId);
	
	Reserve findTopByColBooksIdAndReserveDateAfterOrderByReserveDateAsc(Long ColBooksId,LocalDate reserveDate);
	
	Reserve findByReserveIdAndUserId(Long reserveId,String userId);
	
	List<Reserve> findByUserIdOrderByReserveDateAsc(String userId);
	List<Reserve> findByUserIdAndReserveDate(String userId,LocalDate reserveId);
	List<Reserve> findByUserIdAndReserveDateAfter(String userId,LocalDate reserveId);
	
	
}
