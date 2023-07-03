package com.example.librarySystem.app.admin.books;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminSearchBooksForm {
	
	@NotNull
	@Size(max = 30)
	private String title;
	@NotNull
	@Size(max = 20)
	private String author;
	@Past
	private LocalDate fromDate;
	@Past
	private LocalDate toDate;
	private Integer genreId = 0;
	private Integer publisherId = 0;
	@NotNull
	private String overview;
	
	public void setFromDate(LocalDate fromDate) {
		if(fromDate==null) {
			this.fromDate = LocalDate.of(1900, 1, 1);
		}else {
			this.fromDate=fromDate;
		}
	}
	
	public void setToDate(LocalDate toDate) {
		if(toDate==null) {
			this.toDate = LocalDate.now();
		}else {
			this.toDate=toDate;
		}
	}

}
