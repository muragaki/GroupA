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
import com.example.librarySystem.validator.reserve.ReserveDateValidator;
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
	
	@Autowired
	ReserveDateValidator reserveDateValidator;
	
	@ModelAttribute("reserveDateForm")
	public ReserveDateForm setReserveDateForm() {
		return new ReserveDateForm();
	}
	
	@ModelAttribute("reserveForm")
	public ReserveForm setReserveForm() {
		return new ReserveForm();
	}
	
	@InitBinder("reserveForm")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(reserveValidator);
	}
	
	
	@PostMapping("user/books/reserve")
	public String reserve(@RequestParam("bookId") Integer bookId, ReserveDateForm reserveDateForm, Model model) {
		reserveDateForm.setBooksId(bookId);
		return "/user/books/reserve/reserve";
	}
	
	@PostMapping("user/books/returndate")
	public String returndate(@Validated ReserveDateForm reserveDateForm,ReserveForm reserveForm, BindingResult br ,Model model) {
		if(br.hasErrors()) {
			return "/user/books/reserve/reserve";
		}
		
		reserveForm.setBooksId(reserveDateForm.getBooksId());
		reserveForm.setReserveDate(reserveDateForm.getReserveDate());
		
		int maxReserve = (int)reserveService.searchMaxReservePeriod(reserveDateForm);
		model.addAttribute("maxReserve", maxReserve);
		
		return "/user/books/reserve/returndate";
		
	}
	
	@PostMapping("user/books/reserveconf")
	public String reserveconf(@Validated ReserveForm reserveForm,BindingResult br,Model model) {
		
		if(br.hasErrors()) {
			int maxReserve = (int)reserveService.searchMaxReservePeriod(reserveForm);
			model.addAttribute("maxReserve", maxReserve);
			return "/user/books/reserve/returndate";
		}
		
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		
		reserveService.saveReserve(reserveForm, userId);
		
		return "/user/books";
		
	}

}
