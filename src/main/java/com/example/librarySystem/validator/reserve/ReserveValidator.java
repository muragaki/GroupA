package com.example.librarySystem.validator.reserve;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.librarySystem.app.user.books.reserve.ReserveForm;
import com.example.librarySystem.domain.service.ReserveService;

@Component
public class ReserveValidator implements Validator {
	
	@Autowired
	ReserveService reserveService;

	@Override
	public boolean supports(Class<?> clazz) {
		return ReserveForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		ReserveForm reserveForm = (ReserveForm)target;
		
		if(reserveForm.getScheduledReturnDate() == null) {
			errors.rejectValue("scheduledReturnDate", "com.example.librarySystem.validator.reserve.ReserveValidator.NullMessage");
			return;
		}
		
		if(reserveService.checkReserveSet(reserveForm.getBooksId(), reserveForm.getReserveDate(),-1L).size()==0) {
			errors.rejectValue("reserveDate", "com.example.librarySystem.validator.receive.ReserveEditValidator.NotLendBookMessage");
		}else {
			if(reserveForm.getReserveDate().isAfter(reserveForm.getScheduledReturnDate())) {
				errors.rejectValue("scheduledReturnDate", "com.example.librarySystem.validator.reserve.ReserveValidator.FutuerReserveMessage");
			}

			if(ChronoUnit.DAYS.between(reserveForm.getReserveDate(),reserveForm.getScheduledReturnDate()) >
			reserveService.searchMaxReservePeriod(reserveForm)) {
				errors.rejectValue("scheduledReturnDate","com.example.librarySystem.validator.reserve.ReserveValidator.NotReserveMessage");
			}
		}
	}

}
