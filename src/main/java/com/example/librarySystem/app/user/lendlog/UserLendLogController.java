package com.example.librarySystem.app.user.lendlog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.librarySystem.domain.model.LendLog;
import com.example.librarySystem.domain.service.LendLogService;
import com.example.librarySystem.domain.service.SuperUserDetails;
import com.example.librarySystem.domain.service.SuperUserDetailsService;

@Controller
public class UserLendLogController {
	
	@Autowired
	SuperUserDetailsService superUserDetailsService;
	
	@Autowired
	LendLogService lendLogService;
	
	@GetMapping("user/lendlog")
	public String lendlog(Model model) {
		
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		
		List<LendLog> lendloglist = lendLogService.findUserList(userId);
		
		model.addAttribute("lendloglist",lendloglist);
		
		return "/user/lendlog/lendlog";
	}

}
