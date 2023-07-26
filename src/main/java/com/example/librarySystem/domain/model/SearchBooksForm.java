package com.example.librarySystem.domain.model;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchBooksForm {

	/**
	 * 書名
	 * 30文字以内
	 */
	@Size(max = 30)
	private String title;
	
	/**
	 * 著者名
	 * 20文字以内
	 */
	@Size(max = 20)
	private String author;
	
	/**
	 * 発売日の検索開始年月日
	 * 過去限定
	 */
	@Past
	private LocalDate fromDate;
	
	/**
	 * 発売日の検索終了年月日
	 * 過去限定
	 */
	@Past
	private LocalDate toDate;
	
	/**
	 * ジャンル名
	 */
	private String genreName;
	
	/**
	 * 出版社名
	 */
	private String publisherName;
	
	/**
	 * 概要
	 */
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
