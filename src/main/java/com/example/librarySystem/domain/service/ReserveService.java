package com.example.librarySystem.domain.service;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.app.user.books.reserve.DayMaxPeriod;
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
	
	public final long MAX_RESERVE_PERIOD =7L;
	public final long NON_PESERVE_ID = -1L;
	
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
		return getReserveColBooksId(reserveForm.getBooksId(),reserveForm.getReserveDate(),reserveForm.getScheduledReturnDate(),this.NON_PESERVE_ID);
	}
	
	
	public Long getReserveColBooksId(Integer booksId, LocalDate reserveDate, LocalDate scheduledReturnDate,Long reserveId) {
		TreeSet<Long> colBooksSet = this.checkReserveSet(booksId,reserveDate,reserveId);
		
		if(colBooksSet.size()==0) {
			return this.NON_PESERVE_ID;
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
	
	public TreeSet<Long> checkAvailableBooksSet(Integer booksId){

		TreeSet<Long> availableBooksSet = new TreeSet<>();

		List<SituationName> situationNamelist = new ArrayList<SituationName>(Arrays.asList(SituationName.AVAILABLE,SituationName.LENDING));
		List<ColBooks> colBooksList = colBooksRepository.findByBooksIdAndSituationNameIn(booksId, situationNamelist);

		if(colBooksList.isEmpty()) {
			return availableBooksSet;
		}

		for(ColBooks cb : colBooksList) {
			availableBooksSet.add(cb.getColBooksId());
		}
		
		return availableBooksSet;
	}
	
	
	public TreeSet<Long> checkReserveSet(TreeSet<Long> availableBooksSet,LocalDate reserveDate,Long reserveId){
		
		TreeSet<Long> colBooksSet = new TreeSet<>(availableBooksSet);
		
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
	
	public TreeSet<Long> checkReserveSet(Integer booksId,LocalDate reserveDate,Long reserveId){
		return this.checkReserveSet(checkAvailableBooksSet(booksId), reserveDate, reserveId);
	}
	
	public TreeSet<Long> checkReserveSet(ReserveDateForm reserveDateForm) {
		return this.checkReserveSet(reserveDateForm.getBooksId(), reserveDateForm.getReserveDate(),this.NON_PESERVE_ID);
	}
	
	public TreeSet<Long> checkReserveSet(ReserveForm reserveForm) {
		return this.checkReserveSet(reserveForm.getBooksId(), reserveForm.getReserveDate(),this.NON_PESERVE_ID);
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
	
	public long searchMaxReservePeriod(TreeSet<Long> availableBooksSet,LocalDate reserveDate,Long reserveId) {
		return searchMaxReservePeriod(checkReserveSet(availableBooksSet,reserveDate,reserveId),reserveDate);
	}
	
	public long searchMaxReservePeriod(Integer booksId,LocalDate reserveDate,Long reserveId) {
		return searchMaxReservePeriod(checkReserveSet(booksId,reserveDate,reserveId),reserveDate);
	}
	
	public long searchMaxReservePeriod(ReserveDateForm reserveDateForm) {
		return searchMaxReservePeriod(reserveDateForm.getBooksId(),reserveDateForm.getReserveDate(),this.NON_PESERVE_ID);
	}
	
	public long searchMaxReservePeriod(ReserveForm reserveForm) {
		return searchMaxReservePeriod(reserveForm.getBooksId(),reserveForm.getReserveDate(),this.NON_PESERVE_ID);
	}
	
	
	
	
	public Reserve readReserve(long id,String userId) {
		return reserveRepository.findByReserveIdAndUserId(id,userId);
	}
	
	public void deleteReserve(Long reserveId) {
		reserveRepository.deleteById(reserveId);
	}
	
	public List<List<DayMaxPeriod>> findMaxPeriodList(Integer booksId){
		
		List<List<DayMaxPeriod>> monthList = new ArrayList<List<DayMaxPeriod>>();
		LocalDate startDay = LocalDate.now();
		
		List<DayMaxPeriod> weekList = new ArrayList<DayMaxPeriod>();
	
		if(!startDay.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
			int minus = startDay.getDayOfWeek().getValue();
			if(minus == DayOfWeek.SUNDAY.getValue()) {
				minus = 0;
			}
			for(int i = 0 ; i <= minus ; i++) {
				weekList.add(new DayMaxPeriod( null , -1L));
			}

		}
		
		TreeSet<Long> availableBooksSet = checkAvailableBooksSet(booksId);
		
		for(int i = 1 ; i <= 30 ; i++) {
			DayMaxPeriod day = new DayMaxPeriod();
			day.setDay(startDay.plusDays(i));
			day.setMaxPeriod(this.searchMaxReservePeriod(availableBooksSet, day.getDay(), this.NON_PESERVE_ID));
			weekList.add(day);
			if(day.getDay().getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
				monthList.add(weekList);
				weekList = new ArrayList<DayMaxPeriod>();
			}
		}
		
		if(!weekList.isEmpty()) {
			for(int i = weekList.get(weekList.size()-1).getDay().getDayOfWeek().getValue() ; i < DayOfWeek.SATURDAY.getValue() ;i++) {
				weekList.add(new DayMaxPeriod(null, -1L));
			}
			monthList.add(weekList);
		}
		
		return monthList;
	
	}
	
}
