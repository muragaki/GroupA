package com.example.librarySystem.app.user.books;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LendForm {
	
	@NotNull
	private Integer bookId;
	@NotNull
	public LocalDate reserveDate;
	
	private LocalDate scheduledReturnDate;
	
	private LocalDate minReturnDate;
	private LocalDate maxReturnDate;

}
