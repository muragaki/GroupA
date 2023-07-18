package com.example.librarySystem.app.user.books.reserve;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DayMaxPeriod {
	
	private LocalDate day;
	private Long maxPeriod;

}
