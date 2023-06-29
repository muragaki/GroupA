package com.example.librarySystem.app.admin.books;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.librarySystem.domain.model.Books;
import com.example.librarySystem.domain.service.BooksService;

@Controller
public class BooksController {
	
	@Autowired
	BooksService booksService;

	@GetMapping("admin/books")
	public String books(Model model) {
		
		System.out.println("-----1-----");
		List<Books> bookslist = booksService.readAll();
		
		model.addAttribute("bookslist", bookslist);
		
		return "/admin/books/books";
	}
	
	
	@GetMapping("admin/books/booksedit")
	public String booksEdit() {
		return "/admin/books/booksedit";
	}
}
