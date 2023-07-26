package com.example.librarySystem.app.admin.user;

import java.util.List;

import com.example.librarySystem.domain.model.RoleName;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {
	
	/**
	 * ユーザー名
	 * 正規表現 英数字限定
	 * 8文字以上 15文字以内
	 */
	@Pattern(regexp="[a-zA-Z0-9]*")
	@Size(min = 8 ,max = 15)
	private String username;
	
	/**
	 * ファーストネーム（名前)
	 * 1文字以上 10文字以内 未入力禁止
	 */
	@NotBlank
	@Size(min=1, max=10)
	private String firstname;
	
	/**
	 * ラストネーム（苗字)
	 * 1文字以上 10文字以内 未入力禁止
	 */
	@NotBlank
	@Size(min=1, max=10)
	private String lastname;
	
	/**
	 * 利用者権限
	 */
	private RoleName rolename;
	
	/**
	 * 権限リスト
	 */
	private List<RoleName> roleNameList;
	
}
