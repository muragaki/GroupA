package com.example.librarySystem.app.admin.books;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ColBooksForm {

	@NotNull
	private Integer bookId;
	@NotNull
	private LocalDate registrationDate;
	
}
