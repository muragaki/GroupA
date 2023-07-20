package com.example.librarySystem.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarySystem.domain.model.Genre;


/**
 * GenreRepositoryクラス
 * 
 * エンティティ型：Genre
 * 主キー型：Integer
 * 
 * @author 3030673
 *
 */
public interface GenreRepository extends JpaRepository<Genre, Integer> {

	/**
	 * 
	 * ジャンルIDの昇降順
	 * @return List Genre
	 */
	List<Genre> findAllByOrderByGenreId();
}
