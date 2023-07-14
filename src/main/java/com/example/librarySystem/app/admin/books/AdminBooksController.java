package com.example.librarySystem.app.admin.books;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.example.librarySystem.domain.model.Publisher;
import com.example.librarySystem.domain.model.SearchBooksForm;
import com.example.librarySystem.domain.model.SituationName;
import com.example.librarySystem.domain.service.BooksService;
import com.example.librarySystem.domain.service.ColBooksService;
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
	
	@Autowired
	ColBooksService colBooksService;
		
	@ModelAttribute("adminBooksForm")
	public AdminBooksForm setAdminBookForm() {
		return new AdminBooksForm();
	}
	
	@ModelAttribute("colBooksForm")
	public ColBooksForm setColBookForm() {
		return new ColBooksForm();
	}
	
	@ModelAttribute("searchBooksForm")
	public SearchBooksForm setSearchBooksForm() {
		return new AdminSearchBooksForm();
	}
	
	@InitBinder("searchBooksForm")
	public void initBinderForuserSerchBooksForm(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping("admin/books")
	public String books(SearchBooksForm searchBooksForm ,Model model) {
		
		System.out.println(searchBooksForm);
		
		List<Books> bookslist = booksService.searchBooks(searchBooksForm);
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
	
	
	@GetMapping("admin/books/newbooks")
	public String newbooks(Model model) {
		
		List<Genre> genrelist = genreService.readAll();
		List<Publisher> publisherlist = publisherService.readAll();
		
		model.addAttribute("genrelist", genrelist);
		model.addAttribute("publisherlist", publisherlist);
		
		return "admin/books/newbooks";
	}
	
	@PostMapping("admin/books/booksave")
	public String booksAdd(@Validated AdminBooksForm adminBooksForm, BindingResult br, Model model, RedirectAttributes redirectAttributes) {
		
		if(br.hasErrors()) {
			
			List<Genre> genrelist = genreService.readAll();
			List<Publisher> publisherlist = publisherService.readAll();
			
			model.addAttribute("genrelist", genrelist);
			model.addAttribute("publisherlist", publisherlist);
			return "admin/books/newbooks";
		}
		
		Books book = new Books(adminBooksForm.getBookId(), adminBooksForm.getTitle(), adminBooksForm.getAuthor() , adminBooksForm.getReleaseDate()
							  ,adminBooksForm.getGenreId(), null, adminBooksForm.getPublisherId(), null, adminBooksForm.getOverview());
		
		book = booksService.saveBook(book);
		
		redirectAttributes.addAttribute("id", book.getBookId());
		
		return "redirect:/admin/books/booksconf/{id}";
	}
	
	@GetMapping("admin/books/booksconf/{id}")
	public String booksconf(@PathVariable Integer id ,Model model) {
		Books book = booksService.readByBooksId(id);
		model.addAttribute("book", book);
		return "admin/books/bookconf";
	}
	
	@RequestMapping("admin/books/edit/{id}")
	public String edit(@PathVariable Integer id,AdminBooksForm adminBooksForm,Model model) {
		Books book = booksService.readByBooksId(id);
		
		adminBooksForm.setAdminBooksForm(book);
		
		List<Genre> genrelist = genreService.readSearchAll();
		List<Publisher> publisherlist = publisherService.readSearchAll();
		
		model.addAttribute("genrelist", genrelist);
		model.addAttribute("publisherlist", publisherlist);
		return "admin/books/bookedit";
		
	}
	
	@RequestMapping("admin/books/addcolbook/{id}")
	public String addColBooks(@PathVariable Integer id,ColBooksForm colBooksForm, Model model) {
		
		List<ColBooks> colBooksList = colBooksService.findBookId(id);
		
		colBooksForm.setBookId(id);
		colBooksForm.setRegistrationDate(LocalDate.now());
		
		List<SituationName> situationList = Arrays.asList(SituationName.AVAILABLE,SituationName.LENDING,SituationName.NOT_AVAILABLE);
		
		model.addAttribute("colBooksList", colBooksList);
		model.addAttribute("situationlist", situationList);
		
		return "admin/books/colbooks";
		
	}
	
	@PostMapping("admin/books/colbooksadd")
	public String saveColBooks(@Validated ColBooksForm colBooksForm ,BindingResult br,Model model,RedirectAttributes redirectAttributes) {

		if(br.hasErrors()) {
			List<ColBooks> colBooksList = colBooksService.findBookId(colBooksForm.getBookId());
			model.addAttribute("colBooksList", colBooksList);
			return "admin/books/colbooks"; 
		}
		
		colBooksService.addColBooks(colBooksForm.getBookId(), colBooksForm.getRegistrationDate());
		
		redirectAttributes.addAttribute("id",colBooksForm.getBookId());
		
		return "redirect:/admin/books/addcolbook/{id}";
	}
	
	@PostMapping("admin/books/updatesituation")
	public String updateSituation(@RequestParam Long colBooksId,@RequestParam SituationName situationName, RedirectAttributes redirectAttributes) {
		
		System.out.println(situationName);
		
		ColBooks colbooks = colBooksService.readColBooksId(colBooksId);
		colbooks.setSituationName(situationName);
		
		colBooksService.saveColBooks(colbooks);
		redirectAttributes.addAttribute("id", colbooks.getBooksId());
		
		return "redirect:/admin/books/addcolbook/{id}";
	}
	
}
