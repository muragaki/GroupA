package com.example.librarySystem.domain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarySystem.app.admin.lendlog.SearchLendLogForm;
import com.example.librarySystem.domain.model.LendLog;
import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.repository.LendLogRepository;


/**
 * 
 * LendLogService
 * 
 * @author 3030673
 *
 */
@Service
public class LendLogService {

	@Autowired
	LendLogRepository lendLogRepository;
	
	
	/**
	 * userIdに一致するデータをリストで取得
	 * @param userId
	 * @return List LendLog
	 */
	public List<LendLog> findUserList(String userId){
		return lendLogRepository.findByUserId(userId);
	}
	
	/**
	 * 現在の時刻を追加し、データベースへ登録
	 * @param lending
	 */
	public void saveLendingNow(Lending lending) {
		LendLog lendLog = new LendLog(lending);
		lendLog.setReturnDateTime(LocalDateTime.now().minusSeconds(1));		//プログラム、データベース間の時差によるエラー防止のため-1秒
		lendLogRepository.save(lendLog);
	}
	
	/**
	 * LendLogテーブルの全データをリストで取得
	 * @return List LendLog
	 */
	public List<LendLog> findAll(){
		return lendLogRepository.findAll();
	}
	
	/**
	 * 条件に一致するLendLogデータをリストで取得
	 * @param searchLendLogForm
	 * @return List LendLog
	 */
	public List<LendLog> findSearch(SearchLendLogForm searchLendLogForm){
		
		//検索用変数の宣言、初期化
		Integer bookId = 0;
		Integer identifyNumber = 0;
		String title = "%";
		String author = "%";
		LocalDateTime fromLoanDateTime = LocalDateTime.of(1,1,1,1,1,1);
		LocalDateTime toLoanDateTime = LocalDateTime.now();
		LocalDateTime fromReturnDate = LocalDateTime.now().minusYears(1);
		LocalDateTime toReturnDate = LocalDateTime.now().plusYears(1);
		
		//入力があるデータを代入
		if(searchLendLogForm.getBooksId() != null) {
			bookId = searchLendLogForm.getBooksId();
		}
		
		if(searchLendLogForm.getIdentifyNumber() != null) {
			identifyNumber = searchLendLogForm.getIdentifyNumber();
		}
		
		if(searchLendLogForm.getTitle() != null) {
			title = "%" + searchLendLogForm.getTitle() + "%";
		}
		
		if(searchLendLogForm.getAuthor() != null) {
			author = "%" + searchLendLogForm.getAuthor() + "%";
		}
		
		if(searchLendLogForm.getFromLoanDate() != null) {
			fromLoanDateTime = searchLendLogForm.getFromLoanDate().atStartOfDay();	//開始時刻00:00:00に追加し代入
		}
		
		if(searchLendLogForm.getToLoanDate() != null) {
			toLoanDateTime = searchLendLogForm.getToLoanDate().atTime(23, 59, 59);	//終了時刻23:59:59に追加し代入
		}
		
		if(searchLendLogForm.getFromReurnDate() != null) {
			fromReturnDate = searchLendLogForm.getFromReurnDate().atStartOfDay();	//開始時刻00:00:00を追加し代入
		}
		
		if(searchLendLogForm.getToReturnDate() != null) {
			toReturnDate = searchLendLogForm.getToReturnDate().atTime(23, 59, 59);	//終了時刻23:59:59を追加し代入
		}
		
		return lendLogRepository.findSearch(bookId, identifyNumber,title, author,
											fromLoanDateTime,toLoanDateTime,fromReturnDate,toReturnDate);
	}
		
}
