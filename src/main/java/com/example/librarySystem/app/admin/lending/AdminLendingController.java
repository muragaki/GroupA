package com.example.librarySystem.app.admin.lending;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.service.LendingService;

@Controller
public class AdminLendingController {
	
	@Autowired
	LendingService lendingService;

	@RequestMapping("admin/lending")
	public String lendinglist(Model model) {
		List<Lending> lendinglist = lendingService.findAll();

	
		model.addAttribute("lendinglist", lendinglist);
	
		return "admin/lending/lendinglist";
	}

}