package com.example.librarySystem.app.user.lending;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.librarySystem.domain.model.ColBooks;
import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.model.SituationName;
import com.example.librarySystem.domain.service.ColBooksService;
import com.example.librarySystem.domain.service.LendLogService;
import com.example.librarySystem.domain.service.LendingService;
import com.example.librarySystem.domain.service.SuperUserDetails;


/**
 * 
 * UserLendingControllerクラス
 * 
 * 利用者 貸出コントローラー
 * 
 * @author 中尾 寿晃
 *
 */
@Controller
public class UserLendingController {
	
	@Autowired
	LendingService lendingService;
	
	@Autowired
	LendLogService lendLogService;
	
	@Autowired
	ColBooksService colBooksService;
	
	/**
	 * lendinglistメソッド
	 * 
	 * 貸出一覧
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("user/lending")
	public String lendinglist(Model model) {
		
		//利用者のユーザーIDから貸出リストを取得しビューへ渡す
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		
		List<Lending> lendinglist = lendingService.findUserList(userId);
		
		model.addAttribute("lendinglist", lendinglist);
		
		return "user/lending/lending";
	}

	/**
	 * returnLendメソッド
	 * 
	 * 返却確認
	 * 
	 * @param lendingId
	 * @param model
	 * @return
	 */
	@PostMapping("user/lending/return")
	public String returnLend(@RequestParam Long lendingId,Model model) {
		
		//選択された貸出書籍の詳細を取得しビューへ渡す
		Lending lending = lendingService.readByLendingId(lendingId);
		
		model.addAttribute("lending", lending);
		
		return "user/lending/return";
		
	}
	
	/**
	 * returnconfメソッド
	 * 
	 * 返却確定
	 * 
	 * @param lendingId
	 * @param model
	 * @return
	 */
	@PostMapping("user/lending/returnconf")
	public String returnconf(@RequestParam Long lendingId,Model model) {
		
		//返却する貸出書籍の情報を取得
		Lending lending = lendingService.readByLendingId(lendingId);
		
		//返却する蔵書情報を取得し、利用可能状態へ更新
		ColBooks colBooks = colBooksService.readColBooksId(lending.getColBooksId());
		colBooks.setSituationName(SituationName.AVAILABLE);
		
		//利用履歴データベースを更新、蔵書データベースを更新、貸出データベースを削除する
		lendLogService.saveLendlogNow(lending);
		colBooksService.saveColBooks(colBooks);
		lendingService.deleteLend(lendingId);
		
		return "redirect:/user/lending";
	}
	
}
