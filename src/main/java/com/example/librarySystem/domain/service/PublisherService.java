package com.example.librarySystem.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.Publisher;
import com.example.librarySystem.domain.repository.PublisherRepository;


/**
 * 
 * PublisherServiceクラス
 * 
 * @author 3030673
 *
 */
@Service
public class PublisherService {
	
	@Autowired
	PublisherRepository publisherRepository;
	
	/**
	 * publisherテーブルの全データを取得
	 * @return
	 */
	public List<Publisher> readAll(){
		return publisherRepository.findAllByOrderByPublisherId();
	}
	
	/**
	 * 検索用publisherデータを取得
	 * @return
	 */
	public List<Publisher> readSearchAll(){
		List<Publisher> list = publisherRepository.findAllByOrderByPublisherId();
		list.add(0, new Publisher(0, "ALL"));
		return list;
	}
	
}
