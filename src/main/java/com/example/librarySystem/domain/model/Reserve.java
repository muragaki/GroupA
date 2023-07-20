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


/**
 * Reserve クラス
 * 
 * 予約DB
 * 
 * @author 3030673
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reserve")
public class Reserve {
	
	
	/**
	 * 予約ID(主キー)
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reserveId;
	
	/**
	 * ユーザーID
	 */
	@NotNull
	private String userId;
	
	/**
	 * ユーザーデータ
	 * ユーザーDBより結合
	 */
	@ManyToOne
	@JoinColumn(name="userId", insertable=false, updatable=false)
	private User user;
	
	/**
	 * 蔵書ID
	 */
	@NotNull
	private Long colBooksId;
	
	/**
	 * 蔵書データ
	 * 蔵書DBより結合
	 */
	@ManyToOne
	@JoinColumn(name="colBooksId", insertable=false, updatable=false)
	private ColBooks colBooks;
	
	/**
	 * 貸出予定日
	 */
	@Future
	private LocalDate reserveDate;
	
	/**
	 * 返却予定日
	 */
	@Future
	private LocalDate scheduledReturnDate;
	
	
	/**
	 * コンストラクタ―
	 * @param userId
	 * @param colBooksId
	 * @param reserveDate
	 * @param scheduledReturnDate
	 */
	public Reserve(@NotNull String userId, @NotNull Long colBooksId, @Future LocalDate reserveDate,
			@Future LocalDate scheduledReturnDate) {
		this.userId = userId;
		this.colBooksId = colBooksId;
		this.reserveDate = reserveDate;
		this.scheduledReturnDate = scheduledReturnDate;
	}
	
	
	

}
