package com.example.librarySystem.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.Books;
import com.example.librarySystem.domain.repository.BooksRepository;

@Service
public class BooksService {

	@Autowired
	BooksRepository booksRepository;
	
	public void saveBook(Books books) {
		booksRepository.save(books);
	}
	
	public List<Books> readAll() {
		return booksRepository.findAllByOrderByBookId();
	}
}
