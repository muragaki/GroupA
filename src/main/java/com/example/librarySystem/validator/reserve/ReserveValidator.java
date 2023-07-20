package com.example.librarySystem.validator.reserve;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.librarySystem.app.user.books.reserve.ReserveForm;
import com.example.librarySystem.domain.service.ReserveService;


/**
 * 
 * ReserveValidatorクラス
 * 
 * @author 3030673
 *
 */
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
		
		//ScheduledReturnDateに入力が無ければエラーを込め、返す
		if(reserveForm.getScheduledReturnDate() == null) {
			errors.rejectValue("scheduledReturnDate", "com.example.librarySystem.validator.reserve.ReserveValidator.NullMessage");
			return;
		}
		
		//入力された貸出予定日に予約可能な蔵書が無ければエラーを込める
		if(reserveService.checkReserveSet(reserveForm.getBooksId(), reserveForm.getReserveDate(),-1L).size()==0) {
			errors.rejectValue("reserveDate", "com.example.librarySystem.validator.receive.ReserveEditValidator.NotLendBookMessage");
		}else {
			
			//貸出予定日が返却予定日よりも後であればエラーを込める
			if(reserveForm.getReserveDate().isAfter(reserveForm.getScheduledReturnDate())) {
				errors.rejectValue("scheduledReturnDate", "com.example.librarySystem.validator.reserve.ReserveValidator.FutuerReserveMessage");
			}

			//貸出予定日と返却予定日の差が、貸出予定日からの最長日数を超えていればエラーを込める
			if(ChronoUnit.DAYS.between(reserveForm.getReserveDate(),reserveForm.getScheduledReturnDate()) >
			reserveService.searchMaxReservePeriod(reserveForm)) {
				errors.rejectValue("scheduledReturnDate","com.example.librarySystem.validator.reserve.ReserveValidator.NotReserveMessage");
			}
		}
	}

}
