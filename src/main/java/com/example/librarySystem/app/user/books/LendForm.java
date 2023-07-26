package com.example.librarySystem.app.user.books;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LendForm {
	
	/**
	 * 書籍ID
	 * 未入力禁止
	 */
	@NotNull
	private Integer bookId;
	
	/**
	 * 貸出日
	 * 未入力禁止
	 */
	@NotNull
	public LocalDate reserveDate;
	
	/**
	 * 返却予定日
	 * 未来限定 未入力禁止
	 */
	@NotNull
	@Future
	private LocalDate scheduledReturnDate;
	
	/**
	 * 最長貸出可能日数
	 */
	private Long maxperiod;

	/**
	 * 最短予約可能年月日
	 */
	private LocalDate minReturnDate;
	
	/**
	 * 最長予約可能年月日
	 */
	private LocalDate maxReturnDate;

}
