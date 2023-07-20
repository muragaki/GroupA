package com.example.librarySystem.validator.lendLog;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.librarySystem.app.admin.lendlog.SearchLendLogForm;


/**
 * 
 * SearchLendLogValidatorクラス
 * 
 * @author 3030673
 *
 */
@Component
public class SearchLendLogValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return  SearchLendLogForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		SearchLendLogForm searchLendLogForm = (SearchLendLogForm)target;
		
		//booksIdに入力がなく、identifyNumberに入力があればエラーを込める
		if(searchLendLogForm.getBooksId() == null && searchLendLogForm.getIdentifyNumber() != null) {
			errors.rejectValue("identifyNumber", "com.example.librarySystem.validator.lendLog.SearchLendLogValidator.NoBookIdMessage");
		}

	}

}
