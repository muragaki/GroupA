package com.example.librarySystem.domain.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "col_books")
public class ColBooks {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long colBooksId;
	private Integer booksId;
	private Integer identifyNumber;
	private LocalDate registrationDate;
	@Enumerated(EnumType.STRING)
	private SituationName situationName;		//権限
	
	@ManyToOne
	@JoinColumn(name="booksId", insertable=false, updatable=false)
	private Books books;
	

}