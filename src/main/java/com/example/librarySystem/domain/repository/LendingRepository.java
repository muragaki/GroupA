package com.example.librarySystem.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarySystem.domain.model.Lending;

public interface LendingRepository extends JpaRepository<Lending, Integer> {
	
	List<Lending> findByUserId(String userId);

}
