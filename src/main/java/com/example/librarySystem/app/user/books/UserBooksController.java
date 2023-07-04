package com.example.librarySystem.app.user.books;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.librarySystem.domain.model.Books;
import com.example.librarySystem.domain.model.Genre;
import com.example.librarySystem.domain.model.Publisher;
import com.example.librarySystem.domain.service.BooksService;
import com.example.librarySystem.domain.service.GenreService;
import com.example.librarySystem.domain.service.PublisherService;

@Controller
public class UserBooksController {
	
	
	@Autowired
	BooksService booksService;
	
	@Autowired
	GenreService genreService;
	
	@Autowired
	PublisherService publisherService;
	
	@ModelAttribute("userSerchBooksForm")
	public UserSearchBooksForm setUserSearchBooksForm() {
		return new UserSearchBooksForm();
	}

	@RequestMapping("user/books")
	public String books(UserSearchBooksForm userSearchBooksForm ,Model model) {
		
		System.out.println(userSearchBooksForm);
		
		List<Books> bookslist = booksService.searchBooks(userSearchBooksForm);
		List<Genre> genrelist = genreService.readSearchAll();
		List<Publisher> publisherlist = publisherService.readSearchAll();
		
		model.addAttribute("bookslist", bookslist);
		model.addAttribute("genrelist", genrelist);
		model.addAttribute("publisherlist", publisherlist);
		
		return "user/books/books";
	}
	
	@PostMapping("user/books/bookssearch")
	public String booksSearch(UserSearchBooksForm usersearchBooksForm ,Model model) {
		
		System.out.println(usersearchBooksForm);
		
		return "forward:/user/books";
	}
}
