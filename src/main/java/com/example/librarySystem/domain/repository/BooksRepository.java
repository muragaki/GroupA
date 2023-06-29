package com.example.librarySystem.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarySystem.domain.model.Books;

public interface BooksRepository extends JpaRepository<Books, Integer> {

	public List<Books> findAllByOrderByBookId();
}
