package com.example.librarySystem.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Lendingクラス
 * 貸出中蔵書DB
 * @author 3030673
 *
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="lending")
public class Lending {
	
	/**
	 * 貸出中蔵書ID(主キー)
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long lendingId;
	
	/**
	 * ユーザーID(外部キー)
	 */
	@NotNull
	private String userId;
	
	/**
	 * ユーザーデータ
	 */
	@ManyToOne
	@JoinColumn(name="userId", insertable=false, updatable=false)
	private User user;
	
	/**
	 * 蔵書ID(外部キー)
	 */
	@NotNull
	private Long colBooksId;
	
	/**
	 * 蔵書データ
	 */
	@ManyToOne
	@JoinColumn(name="colBooksId", insertable=false, updatable=false)
	private ColBooks colBooks;
	@Column(columnDefinition = "TIMESTAMP")
	
	/**
	 * 貸出日
	 */
	@Past
	private LocalDateTime loanDateTime;
	
	/**
	 * 	返却予定日
	 */
	@Column(columnDefinition = "DATE")
	@Future
	private LocalDate scheduledReturnDate;
	
	
	/**
	 * コンストラクタ―
	 * @param userId
	 * @param colBooksId
	 * @param loanDateTime
	 * @param scheduledReturnDate
	 */
	public Lending(@NotNull String userId, @NotNull Long colBooksId,
			@Past LocalDateTime loanDateTime, @Future LocalDate scheduledReturnDate) {
		this.userId = userId;
		this.colBooksId = colBooksId;
		this.loanDateTime = loanDateTime;
		this.scheduledReturnDate = scheduledReturnDate;
	}
}
