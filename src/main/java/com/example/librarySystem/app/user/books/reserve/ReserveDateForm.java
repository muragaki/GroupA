package com.example.librarySystem.app.user.books.reserve;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReserveDateForm {
	
	@NotNull
	private Integer BooksId;
	@NotNull
	private LocalDate reserveDate;

}
