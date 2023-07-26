package com.example.librarySystem.app.user.books.reserve;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveForm {

	/**
	 * 書籍ID
	 * 未入力禁止
	 */
	@NotNull
	private Integer BooksId;
	
	/**
	 * 予約開始日
	 * 未来限定 未入力禁止
	 */
	@NotNull
	@Future
	private LocalDate reserveDate;
	
	/**
	 * 返却予定日
	 * 未来限定 未入力禁止
	 */
	@NotNull
	@Future
	private LocalDate scheduledReturnDate;
	
	/**
	 * 最短予約可能年月日
	 */
	private LocalDate minReturnDate;
	
	/**
	 * 最長予約可能年月日
	 */
	private LocalDate maxReturnDate;
}
