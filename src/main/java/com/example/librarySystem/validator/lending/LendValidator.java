package com.example.librarySystem.validator.lending;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.librarySystem.app.user.books.LendForm;
import com.example.librarySystem.domain.service.ReserveService;


/**
 * 
 * LendValidatorクラス
 * 
 * @author 3030673
 *
 */
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
		
		//reserveDateがnullであればエラーを込め、返す
		if(lendForm.getReserveDate() == null) {
			errors.rejectValue("returnDate", "com.example.librarySystem.validator.lending.LendValidator.NullMessage");
			return;
		}
		
		//scheduledReturnDateがnullであればエラーを込め、返す
		if(lendForm.getScheduledReturnDate() == null) {
			errors.rejectValue("scheduledReturnDate", "com.example.librarySystem.validator.lending.LendValidator.NullMessage");
			return;
		}
		
		//貸出日が返却予定日よりも後ろにあればエラーを込める
		if(lendForm.getReserveDate().isAfter(lendForm.getScheduledReturnDate())) {
			errors.rejectValue("scheduledReturnDate", "com.example.librarySystem.validator.lending.LendValidator.FutureMessage");
		}
		
		//貸出予定日と返却予定日の差が貸出可能日数を超えていればエラーを込める
		if(ChronoUnit.DAYS.between(lendForm.getReserveDate(),lendForm.getScheduledReturnDate()) >
												reserveService.searchMaxReservePeriod(lendForm.getBookId(),lendForm.getReserveDate(),-1L)) {
			errors.rejectValue("scheduledReturnDate","com.example.librarySystem.validator.receive.ReserveEditValidator.NotReserveMessage");
		}
		

	}

}
