	package com.example.librarySystem.domain.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.Books;
import com.example.librarySystem.domain.model.SearchBooksForm;
import com.example.librarySystem.domain.repository.BooksRepository;


/**
 * BooksServiceクラス
 * 
 * @author 3030673
 *
 */
@Service
public class BooksService {

	@Autowired
	BooksRepository booksRepository; 
	
	/**
	 * booksデータベースに追加
	 * @param books
	 * @return Books
	 */
	public Books saveBook(Books books) {
		return booksRepository.save(books);
	}
	
	/**
	 * booksIdのデータを読み込み
	 * @param booksId
	 * @return Books
	 */
	public Books readByBooksId(Integer booksId) {
		return booksRepository.findById(booksId).get();
	}
	
	/**
	 * Booksデータベースの全データを読み込み
	 * @return List Books
	 */
	public List<Books> readAll() {
		return booksRepository.findAllByOrderByBookIdAsc();
	}
	
	/**
	 * Booksデータベースの条件検索
	 * @param searchBooksForm
	 * @return List Books
	 */
	public List<Books> searchBooks(SearchBooksForm searchBooksForm){
		
		SearchBooksForm form = new SearchBooksForm(searchBooksForm);
		
		//genreNameを検索用に加工
		if(form.getGenreName() == null || form.getGenreName().equals("ALL")) {
			form.setGenreName("%");
		}
		
		//titleを検索用に加工
		if(form.getTitle() == null) {
			form.setTitle("%");
		}else {
			form.setTitle("%" + form.getTitle() + "%");
		}
		
		//authorを検索用に加工
		if(form.getAuthor() == null) {
			form.setAuthor("%");
		}else {
			form.setAuthor("%" + form.getAuthor() + "%");
		}
		
		//fromDateを検索用に加工
		if(form.getFromDate() == null) {
			form.setFromDate(LocalDate.of(1, 1, 1));
		}
	
		//toDateを検索用に加工
		if(form.getToDate() == null) {
			form.setToDate(LocalDate.now());
		}
		
		//publisherNameを検索用に加工
		if(form.getPublisherName() == null || form.getPublisherName().equals("ALL")) {
			form.setPublisherName("%");
		}
		
		//overviewを検索用に加工
		if(form.getOverview() == null) {
			form.setOverview("%");
		}else {
			form.setOverview("%" + form.getOverview() + "%");
		}
		
		return booksRepository.findSearch(form.getGenreName(),form.getTitle(),
										  form.getAuthor(),form.getFromDate(),
										  form.getToDate(),form.getPublisherName(),
										  form.getOverview());
	}
	
}
