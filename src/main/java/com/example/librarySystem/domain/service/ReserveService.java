package com.example.librarySystem.domain.service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.app.user.books.reserve.ReserveDateForm;
import com.example.librarySystem.app.user.books.reserve.ReserveForm;
import com.example.librarySystem.domain.model.ColBooks;
import com.example.librarySystem.domain.model.Lending;
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
	
	final long MAX_RESERVE_PERIOD =7L;
	
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
		List<Long> colBooksList = this.searchReserveColBooksIdList(reserveForm);
		
		Reserve reserve = reserveRepository.findTopByColBooksIdInAndReserveDateAfterOrderByReserveDateAsc(colBooksList, reserveForm.getScheduledReturnDate());
		
		if(reserve == null ) {
			return colBooksList.get(0);
		}
		
		return reserve.getColBooksId();
		
		
	}
	
	public List<Long> searchReserveColBooksIdList(ReserveForm reserveForm) {
		
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
	
	
	public TreeSet<Long> checkReserve(Integer booksId,LocalDate reserveDate){
		TreeSet<Long> colBooksSet = new TreeSet<>();
		
		List<SituationName> situationNamelist = new ArrayList<SituationName>(Arrays.asList(SituationName.AVAILABLE,SituationName.LENDING));
		List<ColBooks> colBooksList = colBooksRepository.findByBooksIdAndSituationNameIn(booksId, situationNamelist);
		
		if(colBooksList.isEmpty()) {
			return colBooksSet;
		}
		
		for(ColBooks cb : colBooksList) {
			colBooksSet.add(cb.getColBooksId());
		}
		
		List<Lending> lendinlist = lendingRepository.findByColBooksIdInAndScheduledReturnDateAfter(colBooksSet, reserveDate);
		if(!lendinlist.isEmpty()) {
			for(Lending lending : lendinlist) {
				colBooksSet.remove(lending.getColBooksId());
			}
		}
				
		List<Long> reserveList = reserveRepository.selectByColBooksIdInAnd(colBooksSet,reserveDate);
		if(!reserveList.isEmpty()) {
			colBooksSet.removeAll(reserveList);
		}
		
		System.out.println(colBooksSet);
		
		return colBooksSet;
		
		
	}
	
	public TreeSet<Long> checkReserve(ReserveDateForm reserveDateForm) {
		return this.checkReserve(reserveDateForm.getBooksId(), reserveDateForm.getReserveDate());
	}
	
	
	
	public long searchMaxReservePeriod(TreeSet<Long> colBooksSet , LocalDate reserveDate) {
		long maxPeriod = 0L;
		
		List<Reserve> reserveList = reserveRepository.findByColBooksIdInAndReserveDateAfterOrderByReserveDateDesc(colBooksSet, reserveDate);
		
		for(Reserve reserve : reserveList) {
			System.out.println(reserve);
		}
		
		if(reserveList.size() >= colBooksSet.size()) {
			for(Reserve reserve : reserveList) {
				long period = ChronoUnit.DAYS.between(reserveDate, reserve.getReserveDate());
				if(maxPeriod < period) {
					maxPeriod = period;
					if(maxPeriod >= this.MAX_RESERVE_PERIOD) {
						maxPeriod = this.MAX_RESERVE_PERIOD;
						break;
					}
				}
			}
		}else {
			maxPeriod = this.MAX_RESERVE_PERIOD;;	
		}
		
		return maxPeriod;
	}
	
	public long searchMaxReservePeriod(Integer booksId,LocalDate reserveDate) {
		return searchMaxReservePeriod(checkReserve(booksId,reserveDate),reserveDate);
	}
	
	public long searchMaxReservePeriod(ReserveDateForm reserveDateForm) {
		return searchMaxReservePeriod(reserveDateForm.getBooksId(),reserveDateForm.getReserveDate());
	}
	
	public long searchMaxReservePeriod(ReserveForm reserveForm) {
		return searchMaxReservePeriod(reserveForm.getBooksId(),reserveForm.getReserveDate());
	}
	
	
}
