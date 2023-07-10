package com.example.librarySystem.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarySystem.domain.model.Lending;

public interface LendingRepository extends JpaRepository<Lending, Long> {
	
	List<Lending> findByUserId(String userId);
	
	List<Lending> findByColBooksIdAndScheduledReturnDateAfter(Long colBooskId, LocalDate scheduledReturnDate);
	
	List<Lending> findByColBooksIdInAndScheduledReturnDateAfter(Set<Long> colBooksId , LocalDate scheduledReturnDate);

}
