package com.example.librarySystem.validator.receive;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.librarySystem.app.user.receive.ReserveEditForm;
import com.example.librarySystem.domain.service.ReserveService;

/**
 * 
 * ReserveEditValidatorクラス
 * 
 * @author 3030673
 *
 */
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
		
		//ReserveDateに入力が無ければエラーを込め、返す
		if(reserveEditForm.getReserveDate() == null) {
			errors.rejectValue("reserveDate", "com.example.librarySystem.validator.lending.LendValidator.NullMessage");
			return;
		}
		
		//ScheduledReturnDateに入力が無ければエラーを込め、返す
		if(reserveEditForm.getScheduledReturnDate() == null) {
			errors.rejectValue("scheduledReturnDate", "com.example.librarySystem.validator.lending.LendValidator.NullMessage");
			return;
		}
		
		//入力された貸出予定日に予約可能な蔵書が無ければエラーを込める
		if(reserveService.checkReserveSet(reserveEditForm.getBooksId(), reserveEditForm.getReserveDate(),reserveEditForm.getReserveId()).size()==0) {
			errors.rejectValue("reserveDate", "com.example.librarySystem.validator.receive.ReserveEditValidator.NotLendBookMessage");
		}else {
			
			//貸出予定日が返却予定日よりも後であればエラーを込める
			if(reserveEditForm.getReserveDate().isAfter(reserveEditForm.getScheduledReturnDate())) {
				errors.rejectValue("scheduledReturnDate", "com.example.librarySystem.validator.receive.ReserveEditValidator.FutuerReserveMessage");
			}
			
			//貸出予定日と返却予定日の差が、貸出予定日からの最長日数を超えていればエラーを込める
			if(ChronoUnit.DAYS.between(reserveEditForm.getReserveDate(),reserveEditForm.getScheduledReturnDate()) >
			reserveService.searchMaxReservePeriod(reserveEditForm.getBooksId(),reserveEditForm.getReserveDate(),reserveEditForm.getReserveId())) {
				errors.rejectValue("scheduledReturnDate","com.example.librarySystem.validator.receive.ReserveEditValidator.NotReserveMessage");
			}
		}
		
	}

}
