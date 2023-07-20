package com.example.librarySystem.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * RoleName
 * 
 * ユーザー権限の列挙型
 * @author 3030673
 */
@Getter
@AllArgsConstructor
public enum RoleName implements Values{
	
	/**
	 * 管理者権限
	 * {@value ADMIN}
	 * 
	 */
	ADMIN("ADMIN", "ADMIN"),
	
	/**
	 * 利用者権限
	 * {@value USER}
	 */
	USER("USER", "USER");
	
	private final String value;
    private final String text;

}

