package com.example.librarySystem.app.admin.lendlog;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchLendLogForm {

		private Integer booksId;
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
