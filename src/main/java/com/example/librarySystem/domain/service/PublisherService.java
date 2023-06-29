package com.example.librarySystem.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.Publisher;
import com.example.librarySystem.domain.repository.PublisherRepository;

@Service
public class PublisherService {
	
	@Autowired
	PublisherRepository publisherRepository;
	
	public List<Publisher> readAll(){
		return publisherRepository.findAllByOrderByPublisherId();
	}
	
}
