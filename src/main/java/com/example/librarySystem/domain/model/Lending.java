package com.example.librarySystem.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
	private Long lendingId;
	@NotNull
	private String userId;
	@OneToOne
	@JoinColumn(name="userId", insertable=false, updatable=false)
	private User user;
	@NotNull
	private Long colBooksId;
	@OneToOne
	@JoinColumn(name="colBooksId", insertable=false, updatable=false)
	private ColBooks colBooks;
	@Past
	private LocalDateTime loanDateTime;
	@Future
	private LocalDate scheduledReturnDate;
	
	
	public Lending(@NotNull String userId, @NotNull Long colBooksId,
			@Past LocalDateTime loanDateTime, @Future LocalDate scheduledReturnDate) {
		this.userId = userId;
		this.colBooksId = colBooksId;
		this.loanDateTime = loanDateTime;
		this.scheduledReturnDate = scheduledReturnDate;
	}
}
