package com.example.librarySystem.app.user.lending;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.librarySystem.domain.model.ColBooks;
import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.service.ColBooksService;
import com.example.librarySystem.domain.service.LendLogService;
import com.example.librarySystem.domain.service.LendingService;
import com.example.librarySystem.domain.service.SuperUserDetails;

@Controller
public class UserLendingController {
	
	@Autowired
	LendingService lendingService;
	
	@Autowired
	LendLogService lendLogService;
	
	@Autowired
	ColBooksService colBooksService;
	
	@GetMapping("user/lending")
	public String lendinglist(Model model) {
		
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		
		List<Lending> lendinglist = lendingService.findUserList(userId);
		
		model.addAttribute("lendinglist", lendinglist);
		
		return "user/lending/lending";
	}

	@PostMapping("user/lending/return")
	public String returnLend(@RequestParam Long lendingId,Model model) {
		
		Lending lending = lendingService.readByLendingId(lendingId);
		
		model.addAttribute("lending", lending);
		
		return "user/lending/return";
		
	}
	
	@PostMapping("user/lending/returnconf")
	public String returncnf(@RequestParam Long lendingId,Model model) {
		
		Lending lending = lendingService.readByLendingId(lendingId);
		ColBooks colBooks = colBooksService.readColBooksId(lending.getColBooksId());
		colBooks.setSituation(0);
		
		lendLogService.saveLendingNow(lending);
		colBooksService.saveColBooks(colBooks);
		lendingService.deleteLend(lendingId);
		
		return "redirect:/user/lending";
	}
	
}
