package com.example.librarySystem.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarySystem.domain.model.Publisher;


/**
 * 
 * PublisherRepositoryクラス
 * 
 * エンティティ型：Publisher
 * 主キー型：Integer
 * 
 * @author 3030673
 *
 */
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
	
	/**
	 * 
	 * 出版社IDの昇降順
	 * @return List Publisher
	 */
	List<Publisher> findAllByOrderByPublisherId();

}
