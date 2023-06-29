package com.example.librarySystem.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarySystem.domain.model.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
	
	List<Publisher> findAllByOrderByPublisherId();

}
