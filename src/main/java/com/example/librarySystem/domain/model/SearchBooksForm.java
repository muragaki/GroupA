package com.example.librarySystem.domain.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SearchBooksForm {

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
	@NotNull
	private String genreName;
	@NotNull
	private String publisherName;
	@NotNull
	private String overview;
	
	
	
	public void setTitle(String title) {
		if(title.equals("")) {
			this.title="";
		}else {
			this.title = title;
		}
	}
	
	public void setAuthor(String author) {
		if(author.equals("")) {
			this.author="";
		}else {
			this.author = author;
		}
	}
	
	public void setFromDate(LocalDate fromDate) {
		if(fromDate==null) {
			this.fromDate = LocalDate.of(1000, 1, 1);
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
	
	public void setGenreName(String name) {
		if(name.equals("ALL")) {
			this.genreName = "%";
		}else {
			this.genreName = name;
		}
	}
	
	public void setPublisherName(String name) {
		if(name.equals("ALL")) {
			this.publisherName = "%";
		}else {
			this.publisherName = name;
		}
	}
	
	public void setOverview(String overview) {
		if(overview.equals("")) {
			this.overview="";
		}else {
			this.overview = overview;
		}
	}

	public SearchBooksForm() {
		this.title = "";
		this.author = "";
		this.fromDate = LocalDate.of(1000, 1, 1);
		this.toDate = LocalDate.now();
		this.genreName = "%";
		this.publisherName = "%";
		this.overview = "";
	}
	
	
	
}
