package com.example.librarySystem.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum SituationName implements Values {

	AVAILABLE("AVAILABLE", "AVAILABLE"),					//利用可能
	LENDING("LENDING", "LENDING"),							//貸出中
	NOT_AVAILABELE("NOT_AVAILABLE", "NOT_AVAILABLE");		//貸出不可
	

	private final String value;
	private final String text;

}
