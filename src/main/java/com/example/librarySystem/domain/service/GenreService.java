package com.example.librarySystem.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.Genre;
import com.example.librarySystem.domain.repository.GenreRepository;

@Service
public class GenreService {

	@Autowired
	GenreRepository genreRepository;
	
	public List<Genre> readAll(){
		return genreRepository.findAllByOrderByGenreId();
	}
	
	public List<Genre> readSearchAll(){
		List<Genre> list = genreRepository.findAllByOrderByGenreId();
		list.add(0, new Genre(0,"ALL"));
		return list;
	}
}
