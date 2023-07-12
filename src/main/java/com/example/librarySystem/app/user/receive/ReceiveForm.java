package com.example.librarySystem.app.user.receive;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReceiveForm {
	
	private Long reserveId;
	private Long colBooksId;
	private LocalDate reserveDate;
	private LocalDate scheduledReturnDate;
	
}
