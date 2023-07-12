package com.example.librarySystem.app.user.receive;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import lombok.Data;


@Data
public class ReserveEditForm {
	
	private Long reserveId;
	private Integer booksId;
	@Future
	private LocalDate reserveDate;
	@Future
	private LocalDate scheduledReturnDate;

}
