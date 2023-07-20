package com.example.librarySystem.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarySystem.domain.model.ColBooks;
import com.example.librarySystem.domain.model.SituationName;


/**
 * 
 * ColBooksRepositoryクラス
 * 
 * エンティティ型：ColBooks
 * 主キー型：Long
 * 
 * @author 3030673
 *
 */
public interface ColBooksRepository extends JpaRepository<ColBooks, Long> {
	
	/**
	 * 
	 * BooksIdの一致検索
	 * BooksIdの昇降順
	 * 
	 * @param booksId
	 * @return List ColBooks
	 */
	List<ColBooks> findByBooksIdOrderByColBooksId(Integer booksId);
	
	/**
	 * 
	 * BooksIDの一致検索
	 * IdentifyNumberの降下順
	 * 
	 * @param booksId
	 * @return List ColBooks
	 */
	List<ColBooks> findByBooksIdOrderByIdentifyNumberDesc(Integer booksId);
	
	
	/**
	 * 	
	 * BooksIdとsituationNameの一致検索
	 * @param booksId
	 * @param situationName
	 * @return List ColBooks
	 */
	List<ColBooks> findByBooksIdAndSituationName(Integer booksId,SituationName situationName);

	/**
	 * BooksIdとsituationNameの一部を含む一致検索
	 * @param booksId
	 * @param stiuationName
	 * @return List ColBooks
	 */
	List<ColBooks> findByBooksIdAndSituationNameIn(Integer booksId,List<SituationName> stiuationName);
	
}
