package com.example.librarySystem.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarySystem.domain.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

	List<Genre> findAllByOrderByGenreId();
}
