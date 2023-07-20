package com.example.librarySystem.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.Genre;
import com.example.librarySystem.domain.repository.GenreRepository;

/**
 * 
 * GenreServiceクラス
 * 
 * @author 3030673
 *
 */
@Service
public class GenreService {

	@Autowired
	GenreRepository genreRepository;
	
	/**
	 * 
	 * Genreデータベースの全データを取得
	 * @return List Genre
	 */
	public List<Genre> readAll(){
		return genreRepository.findAllByOrderByGenreId();
	}
	
	/**
	 * 検索用ジャンルリストの取得
	 * @return List Genre
	 */
	public List<Genre> readSearchAll(){
		List<Genre> list = genreRepository.findAllByOrderByGenreId();
		list.add(0, new Genre(0,"ALL"));
		return list;
	}
}
