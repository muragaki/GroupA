package com.example.librarySystem.app.user.books.reserve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.librarySystem.domain.service.ColBooksService;
import com.example.librarySystem.domain.service.LendingService;
import com.example.librarySystem.domain.service.ReserveService;
import com.example.librarySystem.domain.service.SuperUserDetails;
import com.example.librarySystem.validator.reserve.ReserveValidator;

@Controller
public class UserReserveController {
	
	@Autowired
	ColBooksService colBooksService;
	
	@Autowired
	LendingService lendingService;
	
	@Autowired
	ReserveService reserveService;
	
	@Autowired
	ReserveValidator reserveValidator;
	
	
	@ModelAttribute("reserveForm")
	public ReserveForm setReserveForm() {
		return new ReserveForm();
	}
	
	@InitBinder("reserveForm")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(reserveValidator);
	}
	
	
	@PostMapping("user/books/reserve")
	public String reserve(@RequestParam("bookId") Integer bookId, ReserveForm reserveForm, Model model) {
		reserveForm.setBooksId(bookId);
		return "/user/books/reserve/reserve";
	}
	
	@PostMapping("user/books/reserveconf")
	public String reserveconf(@Validated ReserveForm reserveForm,BindingResult br,Model model) {
		System.out.println(reserveForm);
		
		if(br.hasErrors()) {
			return "/user/books/reserve/reserve";
		}
		
		System.out.println(reserveService.checkReserveColBooks(reserveForm));
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		
		reserveService.saveReserve(reserveForm, userId);
		
		return "/user/books";
		
	}

}
