package com.example.librarySystem.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.Books;
import com.example.librarySystem.domain.model.SearchBooksForm;
import com.example.librarySystem.domain.repository.BooksRepository;

@Service
public class BooksService {

	static final int GENREFLAG = 1;
	static final int PUBLISHERFLAG = 2;
	
	@Autowired
	BooksRepository booksRepository;
	
	public Books saveBook(Books books) {
		return booksRepository.save(books);
	}
	
	public Optional<Books> readByBooksId(Integer booksId) {
		return booksRepository.findById(booksId);
	}
	
	public List<Books> readAll() {
		return booksRepository.findAllByOrderByBookId();
	}
	
	public List<Books> searchBooks(SearchBooksForm form){
		
		//int flag = 0;
		List<Books> list = null;
		/*
		if(form.getGenreName() != 0 ){
			flag = GENREFLAG;
		}
		
		if(form.getPublisherId() != 0) {
			flag = PUBLISHERFLAG;
		}
		*/
		// switch(flag) {
		//	case 0:
				list = booksRepository.findSearch(form.getGenreName(),form.getTitle(),form.getAuthor(),form.getFromDate(),form.getToDate(),form.getOverview());
		/*		break;
			case GENREFLAG:
				list = booksRepository.findSearchGenre(form.getGenreName(),form.getTitle(),form.getAuthor(),form.getFromDate(),form.getToDate(),form.getOverview());
				break;
			case PUBLISHERFLAG:
				list = booksRepository.findSearchPublisher(form.getTitle(),form.getAuthor(),form.getPublisherId(),form.getFromDate(),form.getToDate(),form.getOverview());
				break;
			case GENREFLAG+PUBLISHERFLAG:
				list = booksRepository.findSearchGenreAndPublisher(form.getGenreName(),form.getTitle(),form.getAuthor(),form.getPublisherId(),form.getFromDate(),form.getToDate(),form.getOverview());
		}*/
		
		
		return list;
	}
}
