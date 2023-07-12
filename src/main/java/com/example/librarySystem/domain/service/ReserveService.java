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
	
	public List<Reserve> findDayReserveList(String userId,LocalDate day){
		return reserveRepository.findByUserIdAndReserveDate(userId, day);
	}
	
	public List<Reserve> findAfterDayReserveList(String userId,LocalDate day){
		return reserveRepository.findByUserIdAndReserveDateAfter(userId, day);
	}
	
	public boolean checkReserveColBooks(ReserveForm reserveForm) {
		
		if(checkReserveSet(reserveForm).size() == 0) {
			return false;
		}else {
			return true;
		}
		
	}
	
	public Long getReserveColBooksId(ReserveForm reserveForm) {
		return getReserveColBooksId(reserveForm.getBooksId(),reserveForm.getReserveDate(),reserveForm.getScheduledReturnDate(),-1L);
	}
	
	
	public Long getReserveColBooksId(Integer booksId, LocalDate reserveDate, LocalDate scheduledReturnDate,Long reserveId) {
		TreeSet<Long> colBooksSet = this.checkReserveSet(booksId,reserveDate,reserveId);
		
		if(colBooksSet.size()==0) {
			return -1L;
		}
		
		Reserve reserve = reserveRepository.findTopByColBooksIdInAndReserveDateAfterOrderByReserveDateAsc(colBooksSet, scheduledReturnDate);
		
		if(reserve == null ) {
			return colBooksSet.first();
		}
		
		return reserve.getColBooksId();
		
	}
	
	
	public Reserve saveReserve(ReserveForm reserveForm ,String UserId) {
		Long colBooksId = this.getReserveColBooksId(reserveForm);
		Reserve reserve = new  Reserve(UserId, colBooksId, reserveForm.getReserveDate(), reserveForm.getScheduledReturnDate());
		return reserveRepository.save(reserve);
	}
	
	public Reserve saveReserve(Reserve reserve) {
		return reserveRepository.save(reserve);
	}
	
	
	public TreeSet<Long> checkReserveSet(Integer booksId,LocalDate reserveDate,Long reserveId){
		TreeSet<Long> colBooksSet = new TreeSet<>();
		
		List<SituationName> situationNamelist = new ArrayList<SituationName>(Arrays.asList(SituationName.AVAILABLE,SituationName.LENDING));
		List<ColBooks> colBooksList = colBooksRepository.findByBooksIdAndSituationNameIn(booksId, situationNamelist);
		
		if(colBooksList.isEmpty()) {
			return colBooksSet;
		}
		
		for(ColBooks cb : colBooksList) {
			colBooksSet.add(cb.getColBooksId());
		}
		
		List<Lending> lendinlist = lendingRepository.findByColBooksIdInAndScheduledReturnDateGreaterThanEqual(colBooksSet, reserveDate);
		if(!lendinlist.isEmpty()) {
			for(Lending lending : lendinlist) {
				colBooksSet.remove(lending.getColBooksId());
			}
		}
				
		List<Long> reserveList = reserveRepository.getColbooksIdList(colBooksSet,reserveDate,reserveId);
		if(!reserveList.isEmpty()) {
			colBooksSet.removeAll(reserveList);
		}
		
		return colBooksSet;
		
	}
	
	public TreeSet<Long> checkReserveSet(ReserveDateForm reserveDateForm) {
		return this.checkReserveSet(reserveDateForm.getBooksId(), reserveDateForm.getReserveDate(),-1L);
	}
	
	public TreeSet<Long> checkReserveSet(ReserveForm reserveForm) {
		return this.checkReserveSet(reserveForm.getBooksId(), reserveForm.getReserveDate(),-1L);
	}
	
	
	public long searchMaxReservePeriod(TreeSet<Long> colBooksSet , LocalDate reserveDate) {
		long maxPeriod = 0L;
			
		for(Long colBooksId : colBooksSet) {
			
			Reserve reserve = reserveRepository.findTopByColBooksIdAndReserveDateAfterOrderByReserveDateAsc(colBooksId, reserveDate);
			
			if(reserve == null) {
				maxPeriod = this.MAX_RESERVE_PERIOD;
				break;
			}
			
			long period = ChronoUnit.DAYS.between(reserveDate, reserve.getReserveDate().minusDays(1));
			if(maxPeriod < period) {
				maxPeriod = period;
				if(maxPeriod >= this.MAX_RESERVE_PERIOD) {
					maxPeriod = this.MAX_RESERVE_PERIOD;
					break;
				}
			}
		}
		
		return maxPeriod;
	}
	
	public long searchMaxReservePeriod(Integer booksId,LocalDate reserveDate,Long reserveId) {
		return searchMaxReservePeriod(checkReserveSet(booksId,reserveDate,reserveId),reserveDate);
	}
	
	public long searchMaxReservePeriod(ReserveDateForm reserveDateForm) {
		return searchMaxReservePeriod(reserveDateForm.getBooksId(),reserveDateForm.getReserveDate(),-1L);
	}
	
	public long searchMaxReservePeriod(ReserveForm reserveForm) {
		return searchMaxReservePeriod(reserveForm.getBooksId(),reserveForm.getReserveDate(),-1L);
	}
	
	
	public Reserve readReserve(long id,String userId) {
		return reserveRepository.findByReserveIdAndUserId(id,userId);
	}
	
	public void deleteReserve(Long reserveId) {
		reserveRepository.deleteById(reserveId);
	}
	
}
