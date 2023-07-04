package com.example.librarySystem.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	@NotNull
	private Long colBooksId;
	@Past
	private LocalDateTime loanDateTime;
	@Past
	private LocalDateTime returnDateTime;

}
