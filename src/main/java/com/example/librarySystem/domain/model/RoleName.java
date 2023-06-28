package com.example.librarySystem.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleName implements Values{
	ADMIN("ADMIN", "ADMIN"),
	USER("USER", "USER");
	
	private final String value;
    private final String text;

}

