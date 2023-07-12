package com.example.librarySystem.validator.reserve;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.librarySystem.app.user.books.reserve.ReserveDateForm;
import com.example.librarySystem.domain.service.ReserveService;

@Component
public class ReserveDateValidator implements Validator {
	
	@Autowired
	ReserveService reserveService;

	@Override
	public boolean supports(Class<?> clazz) {
		return ReserveDateForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
	
		ReserveDateForm reserveDateForm = (ReserveDateForm)target;
		
		if(reserveDateForm.getReserveDate().isBefore( LocalDate.now())) {
			errors.rejectValue("reserveDate", "com.example.librarySystem.validator.reserve.ReserveDateValidator.FutuerMessage");
			return;
		}
		
		if(reserveService.checkReserveSet(reserveDateForm).size()==0) {
			errors.rejectValue("reserveDate", "com.example.librarySystem.validator.reserve.ReserveDateValidator.NotLendBookMessage");
			return;
		}
		
	}

}
