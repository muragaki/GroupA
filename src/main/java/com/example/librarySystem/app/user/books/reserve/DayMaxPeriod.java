package com.example.librarySystem.app.user.books.reserve;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayMaxPeriod {
	
	/**
	 * 指定年月日
	 */
	private LocalDate day;
	
	/**
	 * 最大予約可能日数
	 */
	private Long maxPeriod;

}
