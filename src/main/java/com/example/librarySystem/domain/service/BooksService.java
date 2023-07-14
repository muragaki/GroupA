package com.example.librarySystem.domain.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.Books;
import com.example.librarySystem.domain.model.SearchBooksForm;
import com.example.librarySystem.domain.repository.BooksRepository;

@Service
public class BooksService {

	@Autowired
	BooksRepository booksRepository;
	
	public Books saveBook(Books books) {
		return booksRepository.save(books);
	}
	
	public Books readByBooksId(Integer booksId) {
		return booksRepository.findById(booksId).get();
	}
	
	public List<Books> readAll() {
		return booksRepository.findAllByOrderByBookIdAsc();
	}
	
	public List<Books> searchBooks(SearchBooksForm searchBooksForm){
		
		SearchBooksForm form = new SearchBooksForm(searchBooksForm);
		
		if(form.getGenreName() == null || form.getGenreName().equals("ALL")) {
			form.setGenreName("%");
		}
			
		if(form.getTitle() == null) {
			form.setTitle("%");
		}else {
			form.setTitle("%" + form.getTitle() + "%");
		}
		
		if(form.getAuthor() == null) {
			form.setAuthor("%");
		}else {
			form.setAuthor("%" + form.getAuthor() + "%");
		}
		
		if(form.getFromDate() == null) {
			form.setFromDate(LocalDate.of(1, 1, 1));
		}
		
		if(form.getToDate() == null) {
			form.setToDate(LocalDate.now());
		}
		
		if(form.getPublisherName() == null || form.getPublisherName().equals("ALL")) {
			form.setPublisherName("%");
		}
		
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
