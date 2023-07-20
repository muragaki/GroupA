package com.example.librarySystem.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Genreクラス
 * 
 * @author 3030673
 *
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "genre")
public class Genre {

	/**
	 * 主キー ジャンルID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer genreId;
	
	/**
	 * ジャンル名
	 * 1文字以上 10文字以内
	 */
	@Size(min=1 ,max = 10)
	private String genreName;
	
}
