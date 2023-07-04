package com.example.librarySystem.domain.service;

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
	
	public List<Books> searchBooks(SearchBooksForm form){
		return booksRepository.findSearch(form.getGenreName(),form.getTitle(),
										  form.getAuthor(),form.getFromDate(),
										  form.getToDate(),form.getPublisherName(),
										  form.getOverview());
	}
	
}
