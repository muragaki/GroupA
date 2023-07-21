package com.example.librarySystem.app.admin.lending;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchLendingForm {

	@Positive
	private Integer booksId;
	@Positive
	private Integer identifyNumber;
	@Size(max = 30)
	private String title;
	@Size(max = 20)
	private String author;
	@Past
	private LocalDate fromLoanDate;
	@Past
	private LocalDate toLoanDate;
	@Past
	private LocalDate fromReurnDate;
	@Past
	private LocalDate toReturnDate;
	
	
}
