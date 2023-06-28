package com.example.librarySystem.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarySystem.domain.model.User;

public interface UserRepository extends JpaRepository<User, String> {

}
