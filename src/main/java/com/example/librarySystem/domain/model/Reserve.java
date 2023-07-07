package com.example.librarySystem.domain.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reserve")
public class Reserve {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reserveId;
	@NotNull
	private String userId;
	@ManyToOne
	@JoinColumn(name="userId", insertable=false, updatable=false)
	private User user;
	@NotNull
	private Long colBooksId;
	@ManyToOne
	@JoinColumn(name="colBooksId", insertable=false, updatable=false)
	private ColBooks colBooks;
	@Future
	private LocalDate reserveDate;
	@Future
	private LocalDate scheduledReturnDate;
	
	
	public Reserve(@NotNull String userId, @NotNull Long colBooksId, @Future LocalDate reserveDate,
			@Future LocalDate scheduledReturnDate) {
		this.userId = userId;
		this.colBooksId = colBooksId;
		this.reserveDate = reserveDate;
		this.scheduledReturnDate = scheduledReturnDate;
	}
	
	
	

}
