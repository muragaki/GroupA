package com.example.librarySystem.app.user.receive;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import lombok.Data;


@Data
public class ReserveEditForm {
	
	/**
	 * 予約ID
	 */
	private Long reserveId;
	
	/**
	 * 書籍ID
	 */
	private Integer booksId;
	
	/**
	 * 貸出予定日
	 * 未来限定
	 */
	@Future
	private LocalDate reserveDate;
	
	/**
	 * 返却予定日
	 * 未来限定
	 */
	@Future
	private LocalDate scheduledReturnDate;

}
