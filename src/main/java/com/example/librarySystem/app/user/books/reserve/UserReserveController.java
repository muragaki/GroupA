package com.example.librarySystem.app.user.books.reserve;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.librarySystem.domain.model.DayMaxPeriod;
import com.example.librarySystem.domain.model.Reserve;
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
	
	
	@ModelAttribute("reserveForm")
	public ReserveForm setReserveForm() {
		return new ReserveForm();
	}
	
	@InitBinder("reserveForm")
	public void initBinderForReserveForm(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(reserveValidator);
	}
	
	
	@PostMapping("user/books/reserve")
	public String reserve(@RequestParam("bookId") Integer bookId,@RequestParam LocalDate reserveDate, ReserveForm reserveForm, Model model) {
		reserveForm.setBooksId(bookId);
		reserveForm.setReserveDate(reserveDate);
		return "/user/books/reserve/reserve";
	}
	
	
	@PostMapping("user/books/reservesave")
	public String reservesave(@Validated ReserveForm reserveForm,BindingResult br,Model model,RedirectAttributes redirectAttributes) {
		
		if(br.hasErrors()) {
			return "/user/books/reserve/reserve";
		}
		
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		
		Reserve reserve = reserveService.saveReserve(reserveForm, userId);
		
		redirectAttributes.addAttribute("id", reserve.getReserveId());
		
		
		return "redirect:/user/books/reserveconf/{id}";
		
	}
	
	@RequestMapping("/user/books/reserveconf/{id}")
	public String reserveConf(@PathVariable Long id,Model model) {
		
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Reserve reserve = reserveService.readReserve(id,userId);

		model.addAttribute("reserve",reserve);
		
		return "/user/books/reserve/reserveconf";
		
	}
	
	@PostMapping("/user/books/reserve/selectday")
	public String selectDay(@RequestParam("bookId") Integer bookId,Model model) {
		List<DayMaxPeriod> monthList = reserveService.findMaxPeriodList(bookId);
		model.addAttribute("monthlist", monthList);
		model.addAttribute("bookId", bookId);
		return "user/books/reserve/selectday";
	}

}
