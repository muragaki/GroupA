package com.example.librarySystem.app.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserMenuController {
	
	@GetMapping("/user/menu")
	public String userMenu(Model model) {
		return "/user/menu";
	}

}
