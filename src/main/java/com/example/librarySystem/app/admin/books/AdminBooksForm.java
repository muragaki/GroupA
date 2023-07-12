package com.example.librarySystem.app.admin.books;

import java.time.LocalDate;

import com.example.librarySystem.domain.model.Books;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminBooksForm {

	private Integer bookId;
	@NotNull
	@Size(min = 1 , max = 30)
	private String title;
	@NotNull
	@Size(min = 1 , max = 20)
	private String author;
	@NotNull
	@Past
	private LocalDate releaseDate;
	private Integer genreId;
	private Integer publisherId;
	private String overview;
	

	public void setAdminBooksForm(Books book){
		this.bookId = book.getBookId();
		this.title = book.getTitle();
		this.author = book.getAuthor();
		this.genreId = book.getGenreId();
		this.publisherId = book.getPublisherId();
		this.releaseDate = book.getReleaseDate();
		this.overview = book.getOverview();
	}
}
