package com.example.librarySystem.app.admin.lendlog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.librarySystem.domain.model.LendLog;
import com.example.librarySystem.domain.service.LendLogService;

@Controller
public class AdminLendLogController {

	@Autowired
	LendLogService lendLogService;

	@GetMapping("admin/lendlog")
	public String lendlnglist(Model model) {
		List<LendLog> lendloglist = lendLogService.findAll();

	
		model.addAttribute("lendloglist", lendloglist);
	
		return "admin/lendlog/lendloglist";
	}
}
