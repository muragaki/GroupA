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

	/**
	 * 書籍ID
	 */
	private Integer bookId;
	/**
	 * 書名
	 * 1文字以上 30文字以内の未入力禁止
	 */
	@NotNull
	@Size(min = 1 , max = 30)
	private String title;
	/**
	 * 著者名
	 * 1文字以上 20文字以内の未入力禁止
	 */
	@NotNull
	@Size(min = 1 , max = 20)
	private String author;
	
	/**
	 * 発売日
	 * 過去の日付指定 未入力禁止
	 */
	@NotNull
	@Past
	private LocalDate releaseDate;
	/**
	 * ジャンルID
	 */
	private Integer genreId;
	
	/**
	 * 出版社ID
	 */
	private Integer publisherId;
	
	/**
	 * 概要
	 */
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
