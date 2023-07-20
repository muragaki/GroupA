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
import com.example.librarySystem.app.user.books.reserve.ReserveForm;
import com.example.librarySystem.domain.model.ColBooks;
import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.model.Reserve;
import com.example.librarySystem.domain.model.SituationName;
import com.example.librarySystem.domain.repository.ColBooksRepository;
import com.example.librarySystem.domain.repository.LendingRepository;
import com.example.librarySystem.domain.repository.ReserveRepository;

/**
 * 
 * ReserveServiceクラス
 * 
 * @author 3030673
 *
 */
@Service
public class ReserveService {
	
	@Autowired
	ReserveRepository reserveRepository;
	
	@Autowired
	ColBooksRepository colBooksRepository;
	
	@Autowired
	LendingRepository lendingRepository;
	
	/**
	 * 予約可能限界日数
	 */
	public final long MAX_RESERVE_PERIOD =7L; 
	
	/**
	 * 未登録
	 */
	public final long NON_PESERVE_ID = -1L;
	
	/**
	 * userIdと一致するReserveデータをリストで取得
	 * @param userId
	 * @return List Reserve
	 */
	public List<Reserve> findUserList(String userId){
		return reserveRepository.findByUserIdOrderByReserveDateAsc(userId);
	}
	
	/**
	 * userIdと一致　及び　dayが一致するデータをリストで取得
	 * @param userId
	 * @param day
	 * @return List Reserve
	 */
	public List<Reserve> findDayReserveList(String userId,LocalDate day){
		return reserveRepository.findByUserIdAndReserveDate(userId, day);
	}
	
	/**
	 * userIdと一致 及び dayより後のデータをリストで獲得
	 * @param userId
	 * @param day
	 * @return List Reserve
	 */
	public List<Reserve> findAfterDayReserveList(String userId,LocalDate day){
		return reserveRepository.findByUserIdAndReserveDateAfter(userId, day);
	}
	
	/**
	 * 予約可能な蔵書が存在するかの判定
	 * @param reserveForm
	 * @return boolean
	 */
	public boolean checkReserveColBooks(ReserveForm reserveForm) {
		
		if(checkReserveSet(reserveForm).size() == 0) {
			return false;
		}else {
			return true;
		}
		
	}
	
	/**
	 * 予約可能な蔵書のColBooksIdを取得
	 * @param reserveForm
	 * @return Long
	 */
	public Long getReserveColBooksId(ReserveForm reserveForm) {
		return getReserveColBooksId(reserveForm.getBooksId(),reserveForm.getReserveDate(),reserveForm.getScheduledReturnDate(),this.NON_PESERVE_ID);
	}
	
	/**
	 * 予約可能な蔵書のcolBooksIdを取得
	 * @param booksId
	 * @param reserveDate
	 * @param scheduledReturnDate
	 * @param reserveId
	 * @return Long
	 */
	public Long getReserveColBooksId(Integer booksId, LocalDate reserveDate, LocalDate scheduledReturnDate,Long reserveId) {
		TreeSet<Long> colBooksSet = this.checkReserveSet(booksId,reserveDate,reserveId);
		
		if(colBooksSet.size()==0) {
			return this.NON_PESERVE_ID;
		}
		
		//返却予定日より後にあるデータの内、貸出予定日の昇降順トップのデータを取得
		Reserve reserve = reserveRepository.findTopByColBooksIdInAndReserveDateAfterOrderByReserveDateAsc(colBooksSet, scheduledReturnDate);
		
		//該当予約データがない場合予約可能蔵書セットのトップのIDを返す
		if(reserve == null ) {
			return colBooksSet.first();
		}
		
		return reserve.getColBooksId();
		
	}
	
	/**
	 * 予約データの登録
	 * @param reserveForm
	 * @param UserId
	 * @return Reserve
	 */
	public Reserve saveReserve(ReserveForm reserveForm ,String UserId) {
		Long colBooksId = this.getReserveColBooksId(reserveForm);
		Reserve reserve = new  Reserve(UserId, colBooksId, reserveForm.getReserveDate(), reserveForm.getScheduledReturnDate());
		return reserveRepository.save(reserve);
	}
	
	/**
	 * 予約データの上書き
	 * @param reserve
	 * @return
	 */
	public Reserve saveReserve(Reserve reserve) {
		return reserveRepository.save(reserve);
	}
	
	/**
	 * 予約可能な蔵書のIDをセットで取得
	 * @param booksId
	 * @return TreeSet Long
	 */
	public TreeSet<Long> checkAvailableBooksSet(Integer booksId){

		TreeSet<Long> availableBooksSet = new TreeSet<>();
		
		//booksIdに一致する貸出可能、利用中の蔵書をリストで取得
		List<SituationName> situationNamelist = new ArrayList<SituationName>(Arrays.asList(SituationName.AVAILABLE,SituationName.LENDING));
		List<ColBooks> colBooksList = colBooksRepository.findByBooksIdAndSituationNameIn(booksId, situationNamelist);
		
		//ない場合セットを空で返す
		if(colBooksList.isEmpty()) {
			return availableBooksSet;
		}

		//colBooksIdを抽出しセットに追加
		for(ColBooks cb : colBooksList) {
			availableBooksSet.add(cb.getColBooksId());
		}
		
		return availableBooksSet;
	}
	
	/**
	 * 利用可能セットから貸出予定日に予約が可能なcolBooksIdを抽出
	 * @param availableBooksSet
	 * @param reserveDate
	 * @param reserveId
	 * @return TreeSet Long
	 */
	public TreeSet<Long> checkReserveSet(TreeSet<Long> availableBooksSet,LocalDate reserveDate,Long reserveId){
		
		//対象セットのクローンを生成
		TreeSet<Long> colBooksSet = new TreeSet<>(availableBooksSet);
		
		//利用中の蔵書の中から返却予定日が予約開始日を超えているものをリストで取得
		List<Lending> lendinlist = lendingRepository.findByColBooksIdInAndScheduledReturnDateGreaterThanEqual(colBooksSet, reserveDate);
		
		//該当colBooksIdをセットから削除
		if(!lendinlist.isEmpty()) {
			for(Lending lending : lendinlist) {
				colBooksSet.remove(lending.getColBooksId());
			}
		}
		
		//予約データから貸出予定日に貸出中になる予定のcolBooksIdをリストで取得
		List<Long> reserveList = reserveRepository.getColbooksIdList(colBooksSet,reserveDate,reserveId);
		
		//該当ColBooksIdをセットから除外
		if(!reserveList.isEmpty()) {
			colBooksSet.removeAll(reserveList);
		}
		
		return colBooksSet;
		
	}
	
	/**
	 * booksIdから貸出予定日に予約が可能なcolBooksIdを抽出
	 * @param booksId
	 * @param reserveDate
	 * @param reserveId
	 * @return TreeSet Long
	 */
	public TreeSet<Long> checkReserveSet(Integer booksId,LocalDate reserveDate,Long reserveId){
		return this.checkReserveSet(checkAvailableBooksSet(booksId), reserveDate, reserveId);
	}
	
	/**
	 * resreveFormのデータから貸出予定日に予約が可能なcolBooksIdを抽出
	 * @param reserveForm
	 * @return
	 */
	public TreeSet<Long> checkReserveSet(ReserveForm reserveForm) {
		return this.checkReserveSet(reserveForm.getBooksId(), reserveForm.getReserveDate(),this.NON_PESERVE_ID);
	}
	
	/**
	 * 予約可能蔵書セットから予約可能最長日数を検索
	 * @param colBooksSet
	 * @param reserveDate
	 * @return long
	 */
	public long searchMaxReservePeriod(TreeSet<Long> colBooksSet , LocalDate reserveDate) {
		long maxPeriod = 0L;
		
		//各colBooksIdの予約可能日数を計算
		for(Long colBooksId : colBooksSet) {
			
			//貸出予定日から最短予約を取得
			Reserve reserve = reserveRepository.findTopByColBooksIdAndReserveDateAfterOrderByReserveDateAsc(colBooksId, reserveDate);
			
			//無ければ最長予約日数代入
			if(reserve == null) {
				maxPeriod = this.MAX_RESERVE_PERIOD;
				break;
			}
			
			//貸出予定日と取得した予約データの貸出予定日の前日までの日数差を計算し現最長値と比較
			long period = ChronoUnit.DAYS.between(reserveDate, reserve.getReserveDate().minusDays(1));
			if(maxPeriod < period) {
				maxPeriod = period;
				if(maxPeriod >= this.MAX_RESERVE_PERIOD) { //可能最長日数を超えていたら可能最長日数に置き換え
					maxPeriod = this.MAX_RESERVE_PERIOD;
					break;
				}
			}
		}
		
		return maxPeriod;
	}
	
	/**
	 * 予約可能最長日数を検索
	 * @param availableBooksSet
	 * @param reserveDate
	 * @param reserveId
	 * @return
	 */
	public long searchMaxReservePeriod(TreeSet<Long> availableBooksSet,LocalDate reserveDate,Long reserveId) {
		return searchMaxReservePeriod(checkReserveSet(availableBooksSet,reserveDate,reserveId),reserveDate);
	}
	
	/**
	 * 予約可能最長日数を検索
	 * @param booksId
	 * @param reserveDate
	 * @param reserveId
	 * @return
	 */
	public long searchMaxReservePeriod(Integer booksId,LocalDate reserveDate,Long reserveId) {
		return searchMaxReservePeriod(checkReserveSet(booksId,reserveDate,reserveId),reserveDate);
	}
	
	/**
	 * 予約可能最長日数を検索
	 * @param reserveForm
	 * @return
	 */
	public long searchMaxReservePeriod(ReserveForm reserveForm) {
		return searchMaxReservePeriod(reserveForm.getBooksId(),reserveForm.getReserveDate(),this.NON_PESERVE_ID);
	}
	
	/**
	 * reserveIdとuserIdが一致するデータを取得
	 * @param id
	 * @param userId
	 * @return Reserve
	 */
	public Reserve readReserve(long id,String userId) {
		return reserveRepository.findByReserveIdAndUserId(id,userId);
	}
	
	/**
	 * reserveIdと一致するデータを削除
	 * @param reserveId
	 */
	public void deleteReserve(Long reserveId) {
		reserveRepository.deleteById(reserveId);
	}
	
	/**
	 * 一定期間内の日ごとの最長予約日数を検索し、１週間ごとのリストにし取得
	 * @param booksId
	 * @return
	 */
	public List<List<DayMaxPeriod>> findMaxPeriodList(Integer booksId){
		
		List<List<DayMaxPeriod>> monthList = new ArrayList<List<DayMaxPeriod>>();
		LocalDate startDay = LocalDate.now();
		
		List<DayMaxPeriod> weekList = new ArrayList<DayMaxPeriod>();
		
		//開始日までの曜日を埋める
		if(!startDay.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
			int minus = startDay.getDayOfWeek().getValue();
			if(minus == DayOfWeek.SUNDAY.getValue()) {
				minus = 0;
			}
			for(int i = 0 ; i <= minus ; i++) {
				weekList.add(new DayMaxPeriod( null , -1L));
			}

		}
		
		//利用可能蔵書のセットを取得
		TreeSet<Long> availableBooksSet = checkAvailableBooksSet(booksId);
		
		
		for(int i = 1 ; i <= 30 ; i++) {
			
			//日ごとの最長日数を取得しweekListへ追加
			DayMaxPeriod day = new DayMaxPeriod();
			day.setDay(startDay.plusDays(i));
			day.setMaxPeriod(this.searchMaxReservePeriod(availableBooksSet, day.getDay(), this.NON_PESERVE_ID));
			weekList.add(day);
			
			//土曜日であればweekListをmonthListに追加し新しくリストを生成
			if(day.getDay().getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
				monthList.add(weekList);
				weekList = new ArrayList<DayMaxPeriod>();
			}
		}
		
		//週が残っていれば土曜日まで埋める
		if(!weekList.isEmpty()) {
			int currentDay = weekList.get(weekList.size()-1).getDay().getDayOfWeek().getValue();
			if(currentDay == 7) {
				currentDay = 0;
			}
			for(int i = currentDay ; i < DayOfWeek.SATURDAY.getValue() ;i++) {
				weekList.add(new DayMaxPeriod(null, -1L));
			}
			monthList.add(weekList);
		}
		
		return monthList;
	
	}
	
}
