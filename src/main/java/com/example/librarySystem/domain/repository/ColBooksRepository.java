package com.example.librarySystem.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarySystem.domain.model.ColBooks;
import com.example.librarySystem.domain.model.SituationName;

public interface ColBooksRepository extends JpaRepository<ColBooks, Long> {
	
	List<ColBooks> findByBooksIdOrderByColBooksId(Integer booksId);
	
	List<ColBooks> findByBooksIdOrderByIdentifyNumberDesc(Integer booksId);
	
	List<ColBooks> findByBooksIdAndSituationName(Integer booksId,SituationName situationName);

	List<ColBooks> findByBooksIdAndSituationNameIn(Integer booksId,List<SituationName> stiuationName);
	
	
}
