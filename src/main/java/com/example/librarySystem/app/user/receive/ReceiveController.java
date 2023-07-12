package com.example.librarySystem.app.user.receive;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.librarySystem.domain.model.ColBooks;
import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.model.Reserve;
import com.example.librarySystem.domain.model.SituationName;
import com.example.librarySystem.domain.service.ColBooksService;
import com.example.librarySystem.domain.service.LendingService;
import com.example.librarySystem.domain.service.ReserveService;
import com.example.librarySystem.domain.service.SuperUserDetails;
import com.example.librarySystem.validator.receive.ReserveEditValidator;

@Controller
public class ReceiveController {
	
	@Autowired
	ReserveService reserveService;
	
	@Autowired
	ColBooksService colBooksService;
	
	@Autowired
	LendingService lendingService;
	
	@Autowired
	ReserveEditValidator reserveEditValidator;
	
	@ModelAttribute("reserveEditForm")
	public ReserveEditForm setReserveEditForm() {
		return new ReserveEditForm();
	}
	
	@InitBinder("reserveEditForm")
	public void initBinderForReserveEditForm(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(reserveEditValidator);
	}
	
	@GetMapping("user/receive")
	public String receive(Model model) {
		
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		
		List<Reserve> toDayReserveList = reserveService.findDayReserveList(userId, LocalDate.now());
		List<Reserve> afterDayReserveList = reserveService.findAfterDayReserveList(userId, LocalDate.now());
		
		model.addAttribute("toDayReserveList", toDayReserveList);
		model.addAttribute("afterDayReserveList", afterDayReserveList);
		
		return "/user/receive/receive";
	}
	
	@PostMapping("user/receive/receiving")
	public String receiving(@RequestParam Long reserveId,Model model) {
		
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Reserve reserve = reserveService.readReserve(reserveId, userId);
		boolean notbook = true;
		
		if(reserve == null) {
			
		}
		
		ColBooks colBooks = colBooksService.readColBooksId(reserve.getColBooksId());
			
		if(colBooks.getSituationName() != SituationName.AVAILABLE) {
			
			if(colBooksService.findLendCheck(reserve.getColBooks().getBooksId())) {


				Long colBooksId = reserveService.getReserveColBooksId(reserve.getColBooks().getBooksId(), reserve.getReserveDate(), reserve.getScheduledReturnDate(), reserveId);
				if(colBooksId == -1) {
					notbook = false;
				}else {
					reserve.setColBooksId(colBooksId);
				}
				
			}else {
				notbook = false;
			}
			
		}
		
		model.addAttribute("notbook", notbook);
		model.addAttribute("reserve", reserve);
		
		return "/user/receive/receiving";
		
	}
	
	@PostMapping("user/receive/receivesave")
	public String receivesave(ReceiveForm receiveForm ,RedirectAttributes redirectAttributes) {
		
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Lending lending = new Lending(userId, receiveForm.getColBooksId(), LocalDateTime.now(), receiveForm.getScheduledReturnDate());
		
		lending=lendingService.saveLend(lending);
		ColBooks colBooks = colBooksService.readColBooksId(lending.getColBooksId());
		
		colBooks.setSituationName(SituationName.LENDING);
		
		colBooksService.saveColBooks(colBooks);
		
		reserveService.deleteReserve(receiveForm.getReserveId());
		
		redirectAttributes.addAttribute("lending", lending);
		
		return "redirect:/user/receive/receiveconf";
		
	}
	
	@RequestMapping("user/receive/receiveconf")
	public String receiveconf(@RequestParam Lending lending,Model model) {
		model.addAttribute("lending", lending);
		return "user/receive/receiveconf";
	}
	
	@RequestMapping("user/receive/reservedetail")
	public String reserveDetail(@RequestParam Long reserveId,Model model) {
		
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Reserve reserve = reserveService.readReserve(reserveId, userId);
		
		model.addAttribute("reserve", reserve);
		
		return "user/receive/reservedetail";
	
 	}
	
	@PostMapping("user/receive/reserveedit")
	public String reserveedit(@RequestParam Long reserveId,ReserveEditForm reserveEditForm, Model model) {
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Reserve reserve = reserveService.readReserve(reserveId, userId);
		
		reserveEditForm.setReserveId(reserveId);
		reserveEditForm.setBooksId(reserve.getColBooks().getBooksId());
		reserveEditForm.setReserveDate(reserve.getReserveDate());
		reserveEditForm.setScheduledReturnDate(reserve.getScheduledReturnDate());
		
		return "user/receive/reserveedit";
	}
	
	@PostMapping("user/receive/reservereconf")
	public String reserveReconf(@Validated ReserveEditForm reserveEditForm, BindingResult bindingResult,RedirectAttributes redirectAttributes) {
		
		if(bindingResult.hasErrors()) {
			return "user/receive/reserveedit";
		}
		
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Reserve reserve = reserveService.readReserve(reserveEditForm.getReserveId(), userId);
		
		reserve.setColBooksId(reserveService.getReserveColBooksId(reserveEditForm.getBooksId(), reserveEditForm.getReserveDate(), reserveEditForm.getScheduledReturnDate(),reserve.getReserveId()));
		reserve.setReserveDate(reserveEditForm.getReserveDate());
		reserve.setScheduledReturnDate(reserveEditForm.getScheduledReturnDate());
		
		reserveService.saveReserve(reserve);
		
		redirectAttributes.addAttribute("reserveId", reserveEditForm.getReserveId());
		
		return "redirect:/user/receive/reservedetail";
		
	}
	
	@PostMapping("/user/receive/reservedelete")
	public String reserveDelete(@RequestParam Long reserveId) {
		reserveService.deleteReserve(reserveId);
		return "redirect:/user/receive";
	}
	
}
