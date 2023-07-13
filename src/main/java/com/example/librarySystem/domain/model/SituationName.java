package com.example.librarySystem.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum SituationName implements Values {

	AVAILABLE("AVAILABLE", "利用可能"),					//利用可能
	LENDING("LENDING", "貸出中"),							//貸出中
	NOT_AVAILABLE("NOT_AVAILABLE", "利用不可");		//貸出不可
	

	private final String value;
	private final String text;

}
