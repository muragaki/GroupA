package com.example.librarySystem.domain.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * booksクラス
 * 
 * @author 3030673
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Books {

	/**
	 * 書籍ID 主キー
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookId;
	
	/**
	 * 著書名
	 * 1文字以上 30文字以内
	 */
	@Size(min = 1 , max = 30)
	private String title;
	
	/**
	 * 作者名
	 * 1文字以上 20文字以内
	 */
	@Size(min = 1 , max = 20)
	private String author;
	
	/**
	 * 発刊日
	 */
	private LocalDate releaseDate;
	
	/**
	 * ジャンルID(外部キー)
	 */
	private Integer genreId;
	/**
	 * ジャンルデータ
	 */
	@ManyToOne
	@JoinColumn(name="genreId", insertable=false, updatable=false)
	private Genre genre;
	
	/**
	 * 出版社ID(外部キー)
	 */
	private Integer publisherId;
	/**
	 * 出版社データ
	 */
	@ManyToOne
	@JoinColumn(name="publisherId", insertable=false, updatable=false)
	private Publisher publisher;
	
	/**
	 * 概要
	 */
	private String overview;
	
}
