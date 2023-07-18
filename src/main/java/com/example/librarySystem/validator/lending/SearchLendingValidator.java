package com.example.librarySystem.validator.lending;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.librarySystem.app.admin.lending.SearchLendingForm;

@Component
public class SearchLendingValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return  SearchLendingForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		SearchLendingForm searchLendingForm = (SearchLendingForm)target;
		
		if(searchLendingForm.getBooksId() == null && searchLendingForm.getIdentifyNumber() != null) {
			errors.rejectValue("identifyNumber", "com.example.librarySystem.validator.lending.SearchLendingValidator.NoBookIdMessage");
		}

	}

}
