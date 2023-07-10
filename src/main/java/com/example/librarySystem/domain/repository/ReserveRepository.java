package com.example.librarySystem.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
	
	Reserve findTopByColBooksIdInAndReserveDateAfterOrderByReserveDateAsc(List<Long> colBooksId,LocalDate reserveDate);
	
	
	@Query("SELECT reserve.colBooksId FROM Reserve reserve "
			+ "WHERE reserve.colBooksId IN ( :colBooksIdSet ) "
			+ "AND ( reserve.reserveDate <= :reserveDate "
			+ "AND reserve.scheduledReturnDate >= :reserveDate) ")
	List<Long> selectByColBooksIdInAnd(Set<Long> colBooksIdSet,LocalDate reserveDate);
	
	List<Reserve> findByColBooksIdInAndReserveDateAfterOrderByReserveDateDesc(Set<Long> ColBooksId,LocalDate reserveDate);
}
