package com.example.librarySystem.app.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.librarySystem.domain.model.LendLog;
import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.model.User;
import com.example.librarySystem.domain.service.LendLogService;
import com.example.librarySystem.domain.service.LendingService;
import com.example.librarySystem.domain.service.SuperUserDetailsService;

@Controller
public class AdminUserController {
	
	@Autowired
	SuperUserDetailsService superUserDetailsService;
	
	@Autowired
	LendingService lendingService;
	
	@Autowired
	LendLogService lendLogService;
	
	
	@RequestMapping("admin/user")
	String userlist(Model model) {
		List<User> userlist = superUserDetailsService.getUserAll();
		model.addAttribute("userlist", userlist);
		return "admin/user/userlist";
	}
	
	
	@RequestMapping("admin/user/userdetail")
	String userdetail(@RequestParam String userId, Model model) {
		List<LendLog> lendLoglist = lendLogService.findUserList(userId);
		List<Lending> lendinglist = lendingService.findUserList(userId);
		User user = superUserDetailsService.findById(userId);
		
		model.addAttribute("lendLoglist", lendLoglist);
		model.addAttribute("lendinglist", lendinglist);
		model.addAttribute("u", user);
		return "admin/user/userdetail";
		
	}

	
}
