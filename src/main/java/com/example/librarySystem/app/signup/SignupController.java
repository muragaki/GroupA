package com.example.librarySystem.app.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.librarySystem.domain.model.RoleName;
import com.example.librarySystem.domain.model.User;
import com.example.librarySystem.domain.service.SuperUserDetailsService;
import com.example.librarySystem.validator.signup.SignupValidator;

@Controller
public class SignupController {
	
	@Autowired
	SuperUserDetailsService superUserDetailsService;
	
	@Autowired
	SignupValidator signupValidator;
	
	@GetMapping("signup")
	public String signup() {
		return "/login/signup";
	}
	
	@ModelAttribute("signupForm")
	public SignupForm setSignupForm() {
		return new SignupForm();
	}
	
	@InitBinder("signupForm")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(signupValidator);
	}
	
	@PostMapping("signup")
	public String signupconf(@ModelAttribute("signupForm") @Validated SignupForm signupForm, BindingResult br,Model model) {
		if(br.hasErrors()) {
			return "/login/signup";
		}
		
		User user = new User(signupForm.getUserId()
							,signupForm.getPassword()
							,signupForm.getFirstName()
							,signupForm.getLastName()
							,RoleName.USER);
		
		superUserDetailsService.userregist(user);
		return "redirect:/login";
		
	}
	

}
