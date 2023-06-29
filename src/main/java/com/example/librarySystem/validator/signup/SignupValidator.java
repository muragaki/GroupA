package com.example.librarySystem.validator.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.librarySystem.app.signup.SignupForm;
import com.example.librarySystem.domain.service.SuperUserDetailsService;

@Component
public class SignupValidator implements Validator {

	@Autowired
	SuperUserDetailsService superUserDetailsService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return SignupForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		SignupForm form = (SignupForm)target;
		
		if(superUserDetailsService.checkId(form.getUserId())) {
			errors.reject("com.example.librarySystem.validator.signup.SignupValidator.message");
		}
		
	}

}
