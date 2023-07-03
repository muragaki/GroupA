package com.example.librarySystem.app.admin.books;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.librarySystem.domain.model.Books;
import com.example.librarySystem.domain.model.Genre;
import com.example.librarySystem.domain.model.Publisher;
import com.example.librarySystem.domain.service.BooksService;
import com.example.librarySystem.domain.service.GenreService;
import com.example.librarySystem.domain.service.PublisherService;

@Controller
public class AdminBooksController {
	
	@Autowired
	BooksService booksService;
	
	@Autowired
	GenreService genreService;
	
	@Autowired
	PublisherService publisherService;
	
	@ModelAttribute("adminBooksForm")
	public AdminBooksForm setAdminBookForm() {
		return new AdminBooksForm();
	}
	
	@ModelAttribute("adminSearchBooksForm")
	public AdminSearchBooksForm setSearchBooksForm() {
		return new AdminSearchBooksForm();
	}

	@RequestMapping("admin/books")
	public String books(AdminSearchBooksForm adminsearchBooksForm ,Model model) {
		
		System.out.println(adminsearchBooksForm);
		
		List<Books> bookslist = booksService.searchBooks(adminsearchBooksForm);
		List<Genre> genrelist = genreService.readSearchAll();
		List<Publisher> publisherlist = publisherService.readSearchAll();
		
		model.addAttribute("bookslist", bookslist);
		model.addAttribute("genrelist", genrelist);
		model.addAttribute("publisherlist", publisherlist);
		
		return "admin/books/books";
	}
	
	@PostMapping("admin/books/bookssearch")
	public String booksSearch(AdminSearchBooksForm adminsearchBooksForm ,Model model) {
		
		System.out.println(adminsearchBooksForm);
		
		return "forward:/admin/books";
	}
	
	
	@GetMapping("admin/books/booksedit")
	public String booksEdit(Model model) {
		
		List<Genre> genrelist = genreService.readAll();
		List<Publisher> publisherlist = publisherService.readAll();
		
		model.addAttribute("genrelist", genrelist);
		model.addAttribute("publisherlist", publisherlist);
		
		return "admin/books/booksedit";
	}
	
	@PostMapping("admin/books/booksadd")
	public String booksAdd(@Validated AdminBooksForm adminBooksForm, BindingResult br, Model model, RedirectAttributes redirectAttributes) {
		
		if(br.hasErrors()) {
			List<Genre> genrelist = genreService.readAll();
			List<Publisher> publisherlist = publisherService.readAll();
			
			model.addAttribute("genrelist", genrelist);
			model.addAttribute("publisherlist", publisherlist);
			return "admin/books/booksedit";
		}
		
		Books book = new Books(null, adminBooksForm.getTitle(), adminBooksForm.getAuthor() , adminBooksForm.getReleaseDate()
							  ,adminBooksForm.getGenreId(), null, adminBooksForm.getPublisherId(), null, adminBooksForm.getOverview());
		
		book = booksService.addBook(book);
		
		redirectAttributes.addAttribute("id", book.getBookId());
		
		return "redirect:/admin/books/booksconf/{id}";
	}
	
	@GetMapping("admin/books/booksconf/{id}")
	public String booksconf(@PathVariable Integer id ,Model model) {
		Books book = booksService.readByBooksId(id).get();
		model.addAttribute("book", book);
		return "admin/books/bookconf";
	}
}
