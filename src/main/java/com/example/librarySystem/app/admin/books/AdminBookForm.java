package com.example.librarySystem.app.admin.books;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminBookForm {

	@Size(min = 1 , max = 30)
	private String title;
	@Size(min = 1 , max = 20)
	private String author;
	@Past
	private LocalDate releaseDate;
	private Integer genreId;
	private Integer publisherId;
	private String overview;
	
}
