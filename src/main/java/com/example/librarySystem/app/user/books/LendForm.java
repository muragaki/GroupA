package com.example.librarySystem.app.user.books;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LendForm {
	
	@NotNull
	private Integer bookId;
	@Future
	private LocalDate scheduledReturnDate;

}
