package com.example.librarySystem.app.admin.lending;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.service.LendingService;

@Controller
public class AdminLendingController {
	
	@Autowired
	LendingService lendingService;
	
	@ModelAttribute("searchLendingForm")
	public SearchLendingForm setSearchLendingForm() {
		return new SearchLendingForm();
	}
	
	@InitBinder("searchLendingForm")
	public void initBinderForSearchLendingForm(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping("admin/lending")
	public String lendinglist(SearchLendingForm searchLendingForm, Model model) {
		
		System.out.println(searchLendingForm);
		
		List<Lending> lendinglist = lendingService.findSearch(searchLendingForm);
		model.addAttribute("lendinglist", lendinglist);
	
		return "admin/lending/lendinglist";
	}

}