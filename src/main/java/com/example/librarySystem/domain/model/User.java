package com.example.librarySystem.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Userクラス
 * @author 3030673
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usr")
public class User {
	
	/**
	 * ユーザーID　主キー
	 */
	@Id
	private String userId;
	
	/**
	 * パスワード
	 */
	private String password;
	
	/**
	 * ファーストネーム（名前)
	 */
	private String firstName;
	
	/**
	 * ラストネーム(苗字)
	 */
	private String lastName;
	
	/**
	 * 権限
	 */
	@Enumerated(EnumType.STRING)
	private RoleName roleName;		//権限
	
}