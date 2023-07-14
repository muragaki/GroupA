package com.example.librarySystem.domain.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchBooksForm {

	@Size(max = 30)
	private String title;
	@Size(max = 20)
	private String author;
	@Past
	private LocalDate fromDate;
	@Past
	private LocalDate toDate;
	@NotNull
	private String genreName;
	@NotNull
	private String publisherName;
	private String overview;
	
	public SearchBooksForm(SearchBooksForm searchBooksForm) {
		this.title = searchBooksForm.getTitle();
		this.author = searchBooksForm.getAuthor();
		this.fromDate = searchBooksForm.getFromDate();
		this.toDate = searchBooksForm.getToDate();
		this.genreName = searchBooksForm.getGenreName();
		this.publisherName = searchBooksForm.getPublisherName();
		this.overview = searchBooksForm.getOverview();
	}
	
}
