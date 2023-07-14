package com.example.librarySystem.domain.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DayMaxPeriod {
	
	private LocalDate day;
	private Long maxPeriod;

}
