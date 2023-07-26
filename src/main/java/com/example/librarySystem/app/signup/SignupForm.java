package com.example.librarySystem.app.signup;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SignupForm {
	
	/**
	 * ユーザー名
	 * 正規表現 英数字限定
	 * 8文字以上 15文字以内
	 */
	@Pattern(regexp="[a-zA-Z0-9]*")
	@Size(min = 8 ,max = 15)
	private String userId;
	
	/**
	 * パスワード
	 * 正規表現 英数字限定
	 * 8文字以上 17文字以内
	 */
	@Pattern(regexp="[a-zA-Z0-9]*")
	@Size(min = 8 , max = 17)
	private String password;
	
	/**
	 * ファーストネーム（名前)
	 * 1文字以上 10文字以内 未入力禁止
	 */
	@Size(min = 1 , max = 10)
	private String firstName;
	
	/**
	 * ラストネーム（苗字)
	 * 1文字以上 10文字以内 未入力禁止
	 */
	@Size(min = 1 , max = 10)
	private String lastName;
	
}
