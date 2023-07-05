package com.example.librarySystem.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lend_log")
public class LendLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long lendlogId;
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
	@Past
	private LocalDateTime loanDateTime;
	@Past
	private LocalDateTime returnDateTime;

}
