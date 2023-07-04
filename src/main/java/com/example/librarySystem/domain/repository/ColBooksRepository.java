package com.example.librarySystem.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarySystem.domain.model.ColBooks;

public interface ColBooksRepository extends JpaRepository<ColBooks, Long> {
	
	List<ColBooks> findByBooksIdOrderByColBooksId(Integer booksId);
	
	List<ColBooks> findByBooksIdOrderByIdentifyNumberDesc(Integer booksId);
	
	List<ColBooks> findByBooksIdAndSituation(Integer booksId,Integer situation);

}
