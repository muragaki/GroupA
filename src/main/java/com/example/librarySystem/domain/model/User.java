package com.example.librarySystem.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usr")
public class User {
	
	@Id
	private String userId;			//ユーザーID	
	private String password;		//パスワード
	private String firstName;		//名前(ファーストネーム)
	private String lastName;		//苗字(ラストネーム
	@Enumerated(EnumType.STRING)
	private RoleName roleName;		//権限
	
}