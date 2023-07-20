package com.example.librarySystem.app.user.books.reserve;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveForm {

	@NotNull
	private Integer BooksId;
	
	@NotNull
	@Future
	private LocalDate reserveDate;
	
	@NotNull
	@Future
	private LocalDate scheduledReturnDate;
	
	private LocalDate minReturnDate;
	private LocalDate maxReturnDate;
}
