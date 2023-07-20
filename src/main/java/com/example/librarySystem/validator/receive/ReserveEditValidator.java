package com.example.librarySystem.validator.receive;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.librarySystem.app.user.receive.ReserveEditForm;
import com.example.librarySystem.domain.service.ReserveService;

@Component
public class ReserveEditValidator implements Validator {
	
	@Autowired
	ReserveService reserveService;

	@Override
	public boolean supports(Class<?> clazz) {
		return ReserveEditForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		ReserveEditForm reserveEditForm = (ReserveEditForm) target;
		
		if(reserveEditForm.getReserveDate() == null) {
			errors.rejectValue("reserveDate", "com.example.librarySystem.validator.lending.LendValidator.NullMessage");
			return;
		}
		
		if(reserveEditForm.getScheduledReturnDate() == null) {
			errors.rejectValue("scheduledReturnDate", "com.example.librarySystem.validator.lending.LendValidator.NullMessage");
			return;
		}
		
		if(reserveService.checkReserveSet(reserveEditForm.getBooksId(), reserveEditForm.getReserveDate(),reserveEditForm.getReserveId()).size()==0) {
			errors.rejectValue("reserveDate", "com.example.librarySystem.validator.receive.ReserveEditValidator.NotLendBookMessage");
		}else {
			
			if(reserveEditForm.getReserveDate().isAfter(reserveEditForm.getScheduledReturnDate())) {
				errors.rejectValue("scheduledReturnDate", "com.example.librarySystem.validator.receive.ReserveEditValidator.FutuerReserveMessage");
			}
			
			if(ChronoUnit.DAYS.between(reserveEditForm.getReserveDate(),reserveEditForm.getScheduledReturnDate()) >
			reserveService.searchMaxReservePeriod(reserveEditForm.getBooksId(),reserveEditForm.getReserveDate(),reserveEditForm.getReserveId())) {
				errors.rejectValue("scheduledReturnDate","com.example.librarySystem.validator.receive.ReserveEditValidator.NotReserveMessage");
			}
		}
		
	}

}
