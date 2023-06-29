package com.example.librarySystem.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarySystem.domain.model.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Integer> {

}
