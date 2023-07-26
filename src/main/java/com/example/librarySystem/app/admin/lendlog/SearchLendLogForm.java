package com.example.librarySystem.app.admin.lendlog;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchLendLogForm {

	/**
	 * 書籍ID
	 * 整数限定
	 */
	@Positive
	private Integer booksId;
	
	/**
	 * 蔵書登録ナンバー
	 * 整数限定
	 */
	@Positive
	private Integer identifyNumber;
	
	/**
	 * 書籍タイトル
	 * 30文字以内
	 */
	@Size(max = 30)
	private String title;
	
	/**
	 * 著者名
	 * 20文字以内
	 */
	@Size(max = 20)
	private String author;
	
	/**
	 * 貸出日の検索開始年月日
	 * 過去限定
	 */
	@Past
	private LocalDate fromLoanDate;
	
	/**
	 * 貸出日の検索修了年月日
	 * 過去限定
	 */
	@Past
	private LocalDate toLoanDate;
	
	/**
	 * 返却日の検索開始年月日
	 * 過去限定
	 */
	@Past
	private LocalDate fromReurnDate;
	
	/**
	 * 返却予定日の検索終了年月日
	 */
	@Past
	private LocalDate toReturnDate;


}
