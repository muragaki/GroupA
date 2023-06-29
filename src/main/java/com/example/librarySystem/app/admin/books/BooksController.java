package com.example.librarySystem.app.admin.books;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.librarySystem.domain.model.Books;
import com.example.librarySystem.domain.model.Genre;
import com.example.librarySystem.domain.model.Publisher;
import com.example.librarySystem.domain.service.BooksService;
import com.example.librarySystem.domain.service.GenreService;
import com.example.librarySystem.domain.service.PublisherService;

@Controller
public class BooksController {
	
	@Autowired
	BooksService booksService;
	
	@Autowired
	GenreService genreService;
	
	@Autowired
	PublisherService publisherService;
	
	@ModelAttribute
	public BookForm setSignupForm() {
		return new BookForm();
	}

	@GetMapping("admin/books")
	public String books(Model model) {
		
		List<Books> bookslist = booksService.readAll();
		
		model.addAttribute("bookslist", bookslist);
		
		return "admin/books/books";
	}
	
	
	@GetMapping("admin/books/booksedit")
	public String booksEdit(Model model) {
		
		List<Genre> genrelist = genreService.readAll();
		List<Publisher> publisherlist = publisherService.readAll();
		
		model.addAttribute("genrelist", genrelist);
		model.addAttribute("publisherlist", publisherlist);
		
		return "admin/books/booksedit";
	}
	
	@PostMapping("admin/books/booksconf")
	public String booksEditConf(@ModelAttribute("bookForm") @Validated BookForm bookForm, BindingResult br, Model model) {
		
		if(br.hasErrors()) {
			List<Genre> genrelist = genreService.readAll();
			List<Publisher> publisherlist = publisherService.readAll();
			
			model.addAttribute("genrelist", genrelist);
			model.addAttribute("publisherlist", publisherlist);
			return "admin/books/booksedit";
		}
		
		Books book = new Books(null, bookForm.getTitle(), bookForm.getAuthor() , bookForm.getReleaseDate()
							  ,bookForm.getGenreId(), null, bookForm.getPublisherId(), null, bookForm.getOverview());
		
		booksService.saveBook(book);
		
		return "redirect:/admin/books";
	}
}
