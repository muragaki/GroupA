package com.example.librarySystem.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="lending")
public class Lending {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer lendingId;
	@NotNull
	private String userId;
	@NotNull
	private Integer colBooksId;
	@Past
	private LocalDateTime loanDateTime;
	@Future
	private LocalDate scheduledReturnDate;
	
	
	
	
}
