package com.example.librarySystem.domain.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.app.user.books.reserve.ReserveForm;
import com.example.librarySystem.domain.model.ColBooks;
import com.example.librarySystem.domain.model.Reserve;
import com.example.librarySystem.domain.model.SituationName;
import com.example.librarySystem.domain.repository.ColBooksRepository;
import com.example.librarySystem.domain.repository.LendingRepository;
import com.example.librarySystem.domain.repository.ReserveRepository;

@Service
public class ReserveService {
	
	@Autowired
	ReserveRepository reserveRepository;
	
	@Autowired
	ColBooksRepository colBooksRepository;
	
	@Autowired
	LendingRepository lendingRepository;
	
	public boolean checkReserveColBooks(ReserveForm reserveForm) {
		
		List<SituationName> situationNamelist = new ArrayList<SituationName>(Arrays.asList(SituationName.AVAILABLE,SituationName.LENDING));
		List<ColBooks> colBooksList = colBooksRepository.findByBooksIdAndSituationNameIn(reserveForm.getBooksId(), situationNamelist);
		
		for(ColBooks colbook : colBooksList) {
			
			if( this.checkReserveSpace(colbook,reserveForm) ) {
				return true;
			}
		}
		
		return false;
	}
	
	public Long getReserveColBooksId(ReserveForm reserveForm) {
		List<Long> colBooksList = this.readReserveColBooksIdList(reserveForm);
		
		List<Reserve> reserveList = reserveRepository.findByColBooksIdInAndReserveDateAfterOrderByReserveDateAsc(colBooksList, reserveForm.getScheduledReturnDate());
		
		if(reserveList.isEmpty()) {
			return colBooksList.get(0);
		}
		
		return reserveList.get(0).getColBooksId();
		
		
	}
	
	public List<Long> readReserveColBooksIdList(ReserveForm reserveForm) {
		
		List<SituationName> situationNamelist = new ArrayList<SituationName>(Arrays.asList(SituationName.AVAILABLE,SituationName.LENDING));
		List<ColBooks> colBooksList = colBooksRepository.findByBooksIdAndSituationNameIn(reserveForm.getBooksId(), situationNamelist);
		List<Long> returnList = new ArrayList<>();
		
		for(ColBooks colbook : colBooksList) {
			
			if( this.checkReserveSpace(colbook,reserveForm) ) {
				returnList.add(colbook.getColBooksId());
				
			}
		}
		
		return returnList;
		
	}
	
	public void saveReserve(ReserveForm reserveForm ,String UserId) {
		Long colBooksId = this.getReserveColBooksId(reserveForm);
		Reserve reserve = new  Reserve(UserId, colBooksId, reserveForm.getReserveDate(), reserveForm.getScheduledReturnDate());
		reserveRepository.save(reserve);
	}
	
	public boolean checkReserveSpace(ColBooks colbook , ReserveForm reserveForm) {
		if(colbook.getSituationName().equals(SituationName.LENDING)) {
			if(lendingRepository.findByColBooksIdAndScheduledReturnDateAfter(colbook.getColBooksId(), reserveForm.getReserveDate()).isEmpty()) {
				if(reserveRepository.checkReserveSpace(colbook.getColBooksId(),reserveForm.getReserveDate(), reserveForm.getScheduledReturnDate()).isEmpty()) {
					return true;
				}
			}
		}else {
			if(reserveRepository.checkReserveSpace(colbook.getColBooksId(),reserveForm.getReserveDate(), reserveForm.getScheduledReturnDate()).isEmpty()) {
				return true;
			}
		}
		return false;
		
	}
	

}
