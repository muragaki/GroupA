package com.example.librarySystem.domain.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Books {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookId;
	@Size(min = 1 , max = 30)
	private String title;
	@Size(min = 1 , max = 20)
	private String author;
	@Past
	private LocalDate releaseDate;
	private Integer genreId;
	@ManyToOne
	@JoinColumn(name="genreId", insertable=false, updatable=false)
	private Genre genre;
	private Integer publisherId;
	@ManyToOne
	@JoinColumn(name="publisherId", insertable=false, updatable=false)
	private Publisher publisher;
	private String overview;
	
}
