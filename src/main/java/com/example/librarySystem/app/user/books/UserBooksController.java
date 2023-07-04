package com.example.librarySystem.app.user.books;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.librarySystem.domain.model.Books;
import com.example.librarySystem.domain.model.ColBooks;
import com.example.librarySystem.domain.model.Genre;
import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.model.Publisher;
import com.example.librarySystem.domain.service.BooksService;
import com.example.librarySystem.domain.service.ColBooksService;
import com.example.librarySystem.domain.service.GenreService;
import com.example.librarySystem.domain.service.LendingService;
import com.example.librarySystem.domain.service.PublisherService;
import com.example.librarySystem.domain.service.SuperUserDetails;

@Controller
public class UserBooksController {
	
	
	@Autowired
	BooksService booksService;
	
	@Autowired
	GenreService genreService;
	
	@Autowired
	PublisherService publisherService;
	
	@Autowired
	ColBooksService colBooksService;
	
	@Autowired
	LendingService lendingService;
	
	@ModelAttribute("lendForm")
	public LendForm setLendForm() {
		return new LendForm();
	}
	
	@ModelAttribute("userSerchBooksForm")
	public UserSearchBooksForm setUserSearchBooksForm() {
		return new UserSearchBooksForm();
	}

	@RequestMapping("user/books")
	public String books(UserSearchBooksForm userSearchBooksForm ,Model model) {
		
		List<Books> bookslist = booksService.searchBooks(userSearchBooksForm);
		List<Genre> genrelist = genreService.readSearchAll();
		List<Publisher> publisherlist = publisherService.readSearchAll();
		
		model.addAttribute("bookslist", bookslist);
		model.addAttribute("genrelist", genrelist);
		model.addAttribute("publisherlist", publisherlist);
		
		return "user/books/books";
	}
	
	@RequestMapping("/user/books/{id}")
	public String bookdetail(@PathVariable Integer id, Model model) {
		
		Books book = booksService.readByBooksId(id);
		if(colBooksService.findLendCheck(id)) {
			model.addAttribute("check", "true");
		}else {
			model.addAttribute("check", "false");
		}
		
		model.addAttribute("book", book);
		
		return "/user/books/bookdetail";
		
	}
	
	@RequestMapping("user/books/lend")
	public String booklend(@RequestParam Integer bookId, @Validated LendForm lendForm, Model model) {
		
		lendForm.setBookId(bookId);
		lendForm.setScheduledReturnDate(LocalDate.now().plusDays(1));	
		
		return "user/books/lend";
	}
	
	@PostMapping("user/books/lendconf")
	public String lendconf(LendForm lendForm, BindingResult br,Model model,RedirectAttributes redirectAttributes) {
		System.out.println(lendForm);
		if(br.hasErrors()) {
			return "user/books/lend";
		}
		
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Long colBooksId = colBooksService.readLendColBooksId(lendForm.getBookId());
		
		Lending lending = new Lending(null, userId, colBooksId,null,LocalDateTime.now(), lendForm.getScheduledReturnDate());
		
		lending=lendingService.saveLend(lending);
		ColBooks colBooks = colBooksService.readColBooksId(lending.getColBooksId());
		
		colBooks.setSituation(1);
		
		colBooksService.saveColBooks(colBooks);
		
		redirectAttributes.addAttribute("lending", lending);
		
		return "redirect:/user/books/lendingconf";
		
	}
	
	@RequestMapping("user/books/lendingconf")
	public String ledingconf(@RequestParam Lending lending,Model model) {
		Books books = booksService.readByBooksId(lending.getColBooks().getBooksId());
		model.addAttribute("lending", lending);
		model.addAttribute("books", books);
		return "user/books/lendingconf";
	}
	
}
