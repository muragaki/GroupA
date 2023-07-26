package com.example.librarySystem.app.admin.books;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ColBooksForm {

	/**
	 * 書籍ID
	 * 未入力禁止
	 */
	@NotNull
	private Integer bookId;
	
	/**
	 * 蔵書登録日
	 * 未入力禁止
	 */
	@NotNull
	private LocalDate registrationDate;
	
}
