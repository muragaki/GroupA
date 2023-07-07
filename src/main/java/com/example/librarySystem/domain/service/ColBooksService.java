package com.example.librarySystem.domain.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.ColBooks;
import com.example.librarySystem.domain.model.SituationName;
import com.example.librarySystem.domain.repository.ColBooksRepository;

@Service
public class ColBooksService {
	
	@Autowired
	ColBooksRepository colBooksRepository;
	
	public List<ColBooks> findBookId(Integer booksId){
		return colBooksRepository.findByBooksIdOrderByColBooksId(booksId);
	}
	
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
	
	public boolean findLendCheck(Integer bookId){
		if(colBooksRepository.findByBooksIdAndSituationName(bookId,SituationName.AVAILABLE).isEmpty()){
			return false;
		}else {
			return true;
		}
	}
	
	public Long readLendColBooksId(Integer bookId) {
		return colBooksRepository.findByBooksIdAndSituationName(bookId, SituationName.AVAILABLE).get(0).getColBooksId();
	}
	
	public ColBooks readColBooksId(Long colBooksId) {
		return colBooksRepository.findById(colBooksId).get();
	}
	
	public void saveColBooks(ColBooks colBooks) {
		colBooksRepository.save(colBooks);
	}
	
}
