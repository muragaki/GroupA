package com.example.librarySystem.app.user.receive;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReceiveForm {
	
	/**
	 * 予約ID
	 */
	private Long reserveId;
	
	/**
	 * 蔵書ID
	 */
	private Long colBooksId;
	
	/**
	 * 貸出予定日
	 */
	private LocalDate reserveDate;
	
	/**
	 * 返却予定日
	 */
	private LocalDate scheduledReturnDate;
	
}
