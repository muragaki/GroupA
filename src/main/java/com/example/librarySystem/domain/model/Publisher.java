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
 * Publisherクラス
 * 
 * @author 3030673
 *
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "publisher")
public class Publisher {
	
	/**
	 * 出版社ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer publisherId;
	
	/**
	 * 出版社名
	 * 1文字以上 10文字以内
	 */
	@Size(min = 1 , max = 10)
	private String publisherName;

}
