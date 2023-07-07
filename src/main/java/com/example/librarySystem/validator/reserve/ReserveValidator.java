package com.example.librarySystem.validator.reserve;

import java.time.LocalDate;

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
		
		if(reserveForm.getReserveDate().isBefore( LocalDate.now())) {
			errors.rejectValue("reserveDate", "明日以降の日付を選択して下さい。");
		}
		
		
		if(reserveForm.getReserveDate().isAfter(reserveForm.getScheduledReturnDate())) {
			errors.rejectValue("scheduledReturnDate", "予約開始日移行の日付を選択して下さい。");
		}
		
		if(!reserveService.checkReserveColBooks(reserveForm)) {
			errors.reject("期間内に貸し出せる書籍がありません。別日を選択してください。");
		}
	}

}
