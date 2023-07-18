package com.example.librarySystem.app.user.books.reserve;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayMaxPeriod {
	
	private LocalDate day;
	private Long maxPeriod;

}
