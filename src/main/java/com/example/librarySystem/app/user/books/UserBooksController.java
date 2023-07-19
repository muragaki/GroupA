package com.example.librarySystem.app.user.books;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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
import com.example.librarySystem.domain.model.SearchBooksForm;
import com.example.librarySystem.domain.model.SituationName;
import com.example.librarySystem.domain.service.BooksService;
import com.example.librarySystem.domain.service.ColBooksService;
import com.example.librarySystem.domain.service.GenreService;
import com.example.librarySystem.domain.service.LendingService;
import com.example.librarySystem.domain.service.PublisherService;
import com.example.librarySystem.domain.service.ReserveService;
import com.example.librarySystem.domain.service.SuperUserDetails;
import com.example.librarySystem.validator.lending.LendValidator;

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
	
	@Autowired
	ReserveService reserveService;
	
	@Autowired
	LendValidator lendValidator;
	
	@ModelAttribute("lendForm")
	public LendForm setLendForm() {
		return new LendForm();
	}
	
	@InitBinder("lendForm")
	public void initBinderForLnedForm(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(lendValidator);
	}
	
	@ModelAttribute("serchBooksForm")
	public SearchBooksForm setSearchBooksForm() {
		return new SearchBooksForm();
	}
	
	@InitBinder("searchBooksForm")
	public void initBinderForuserSerchBooksForm(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping("user/books")
	public String books(@Validated SearchBooksForm searchBooksForm ,BindingResult bindingResult,Model model) {
		
		if(bindingResult.hasErrors()) {
			List<Books> bookslist = booksService.readAll();
			List<Genre> genrelist = genreService.readSearchAll();
			List<Publisher> publisherlist = publisherService.readSearchAll();
			
			model.addAttribute("bookslist", bookslist);
			model.addAttribute("genrelist", genrelist);
			model.addAttribute("publisherlist", publisherlist);
			return "user/books/books";
		}
		
		List<Books> bookslist = booksService.searchBooks(searchBooksForm);
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
		long period = reserveService.searchMaxReservePeriod(id, LocalDate.now() ,-1L);
		
		
		if(colBooksService.findLendCheck(id) && period > 0 ) {
			model.addAttribute("check", "true");
		}else {
			model.addAttribute("check", "false");
		}
		
		model.addAttribute("period", period);
		model.addAttribute("book", book);
		
		return "/user/books/bookdetail";
		
	}
	
	@RequestMapping("user/books/lend")
	public String booklend(@RequestParam Integer bookId,LendForm lendForm, Model model) {
		
		lendForm.setBookId(bookId);
		lendForm.setReserveDate(LocalDate.now());
		lendForm.setMinReturnDate(LocalDate.now().plusDays(1));
		lendForm.setScheduledReturnDate(LocalDate.now().plusDays(1));
		long maxPeriod = reserveService.searchMaxReservePeriod(bookId, LocalDate.now(), reserveService.NON_PESERVE_ID);
		lendForm.setMaxReturnDate(LocalDate.now().plusDays(maxPeriod));
		model.addAttribute("maxperiod", maxPeriod);
		
		return "user/books/lend";
	}
	
	@PostMapping("user/books/lendconf")
	public String lendconf(@Validated LendForm lendForm, BindingResult br,Model model,RedirectAttributes redirectAttributes) {
		
		if(br.hasErrors()) {
			return "user/books/lend";
		}
		
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Long colBooksId = colBooksService.readLendColBooksId(lendForm.getBookId());
		
		Lending lending = new Lending(userId, colBooksId,LocalDateTime.now().minusSeconds(1), lendForm.getScheduledReturnDate());
		
		lending=lendingService.saveLend(lending);
		ColBooks colBooks = colBooksService.readColBooksId(lending.getColBooksId());
		
		colBooks.setSituationName(SituationName.LENDING);
		
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
