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
	@NotNull
	private LocalDate scheduledReturnDate;
	
	private Long maxperiod;
	
	private LocalDate minReturnDate;
	private LocalDate maxReturnDate;

}
