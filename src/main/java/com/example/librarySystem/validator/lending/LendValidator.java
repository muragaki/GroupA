package com.example.librarySystem.validator.lending;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.librarySystem.app.user.books.LendForm;
import com.example.librarySystem.domain.service.ReserveService;

@Component
public class LendValidator implements Validator {
	
	@Autowired
	ReserveService reserveService;

	@Override
	public boolean supports(Class<?> clazz) {
		return LendForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		LendForm lendForm = (LendForm)target;
		
		if(lendForm.getScheduledReturnDate() == null) {
			errors.rejectValue("scheduledReturnDate", "com.example.librarySystem.validator.lending.LendValidator.NullMessage");
			return;
		}
		
		if(lendForm.getReserveDate().isAfter(lendForm.getScheduledReturnDate())) {
			errors.rejectValue("scheduledReturnDate", "com.example.librarySystem.validator.lending.lendValidator.FutureMessage");
		}
		
		if(ChronoUnit.DAYS.between(lendForm.getReserveDate(),lendForm.getScheduledReturnDate()) >
												reserveService.searchMaxReservePeriod(lendForm.getBookId(),lendForm.getReserveDate(),-1L)) {
			errors.rejectValue("scheduledReturnDate","com.example.librarySystem.validator.receive.ReserveEditValidator.NotReserveMessage");
		}
		

	}

}
