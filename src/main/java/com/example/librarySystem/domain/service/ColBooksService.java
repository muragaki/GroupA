package com.example.librarySystem.domain.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.ColBooks;
import com.example.librarySystem.domain.model.SituationName;
import com.example.librarySystem.domain.repository.ColBooksRepository;

/**
 * 
 * ColBooksServicクラス
 * 
 * @author 3030673
 *
 */
@Service
public class ColBooksService {
	
	@Autowired
	ColBooksRepository colBooksRepository;
	
	/**
	 * BooksIdに一致するデータを取得
	 * @param booksId
	 * @return List ColBooks
	 */
	public List<ColBooks> findBookId(Integer booksId){
		return colBooksRepository.findByBooksIdOrderByColBooksId(booksId);
	}
	
	/**
	 * colBooksにデータを追加
	 * @param booksId
	 * @param date
	 */
	public void addColBooks(Integer booksId,LocalDate date) {
		List<ColBooks> list = colBooksRepository.findByBooksIdOrderByIdentifyNumberDesc(booksId);
		Integer number;
		if(list.isEmpty()) {
			number = 1;
		}else {
			number=list.get(0).getIdentifyNumber()+1;
		}
		
		colBooksRepository.save(new ColBooks(null,booksId,number, date, SituationName.AVAILABLE,null));
		
	}
	
	/**
	 * booksIdに一致する利用可能蔵書の検索
	 * @param bookId
	 * @return boolean
	 */
	public boolean findLendCheck(Integer booksId){
		if(colBooksRepository.findByBooksIdAndSituationName(booksId,SituationName.AVAILABLE).isEmpty()){
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * booksIdに一致する利用可能蔵書の昇降順トップのcolBooksIdを取得
	 * @param bookId
	 * @return Long
	 */
	public Long readLendColBooksId(Integer bookId) {
		return colBooksRepository.findByBooksIdAndSituationName(bookId, SituationName.AVAILABLE).get(0).getColBooksId();
	}
	
	/**
	 * colBooksIdに一致するColBooksを獲得
	 * @param colBooksId
	 * @return Colbooks
	 */
	public ColBooks readColBooksId(Long colBooksId) {
		return colBooksRepository.findById(colBooksId).get();
	}
	
	/**
	 * colBooksを上書き
	 * @param colBooks
	 */
	public void saveColBooks(ColBooks colBooks) {
		colBooksRepository.save(colBooks);
	}
	
}
