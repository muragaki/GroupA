package com.example.librarySystem.domain.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * ColBooksクラス
 * 
 * 蔵書管理DB
 * @author 3030673
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "col_books")
public class ColBooks {
	
	/**
	 * 蔵書ID 主キー
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long colBooksId;
	
	/**
	 * 書籍ID
	 */
	private Integer booksId;
	
	/**
	 * 書籍別蔵書ナンバー
	 */
	private Integer identifyNumber;
	
	/**
	 * 蔵書登録日
	 */
	private LocalDate registrationDate;
	
	/**
	 * 蔵書利用状況
	 */
	@Enumerated(EnumType.STRING)
	private SituationName situationName;		
	
	/**
	 * 書籍データ
	 */
	@ManyToOne
	@JoinColumn(name="booksId", insertable=false, updatable=false)
	private Books books;
	

}