package com.example.librarySystem.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleName implements Values{
	ADMIN("ADMIN", "ADMIN"),		//管理者
	USER("USER", "USER");			//利用者
	
	private final String value;
    private final String text;

}

