package com.example.librarySystem.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LendLogクラス
 * 
 * 利用履歴DB
 * 
 * @author 3030673
 *
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lend_log")
public class LendLog {
	
	/**
	 * 利用履歴ID(主キー)
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long lendlogId;
	
	/**
	 * ユーザーID(外部キー)
	 */
	@NotNull
	private String userId;
	
	/**
	 * ユーザーデータ
	 * ユーザーDBより結合
	 */
	@ManyToOne
	@JoinColumn(name="userId", insertable=false, updatable=false)
	private User user;
	
	/**
	 * 蔵書ID(外部キー)
	 */
	@NotNull
	private Long colBooksId;
	
	/**
	 * 蔵書データ
	 * 蔵書DBより結合
	 */
	@ManyToOne
	@JoinColumn(name="colBooksId", insertable=false, updatable=false)
	private ColBooks colBooks;
	
	/**
	 * 貸出日
	 */
	@Column(columnDefinition = "TIMESTAMP")
	@Past
	private LocalDateTime loanDateTime;
	
	/**
	 * 返却日
	 */
	@Column(columnDefinition = "TIMESTAMP")
	@Past
	private LocalDateTime returnDateTime;
	
	
	/**
	 * コンストラクタ―
	 * @param lending
	 */
	public LendLog(Lending lending) {
		this.userId=lending.getUserId();
		this.colBooksId=lending.getColBooksId();
		this.loanDateTime=lending.getLoanDateTime();
	}

}
