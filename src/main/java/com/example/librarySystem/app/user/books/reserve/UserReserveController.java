package com.example.librarySystem.app.user.books.reserve;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.librarySystem.domain.model.Reserve;
import com.example.librarySystem.domain.service.ColBooksService;
import com.example.librarySystem.domain.service.LendingService;
import com.example.librarySystem.domain.service.ReserveService;
import com.example.librarySystem.domain.service.SuperUserDetails;
import com.example.librarySystem.validator.reserve.ReserveValidator;


/**
 * 
 * UserReserveControllerクラス
 * 
 * 利用者 書籍予約コントローラー
 * 
 * @author 中尾 寿晃
 *
 */
@Controller
public class UserReserveController {
	
	@Autowired
	ColBooksService colBooksService;
	
	@Autowired
	LendingService lendingService;
	
	@Autowired
	ReserveService reserveService;
	
	@Autowired
	ReserveValidator reserveValidator;
	
	//form-backing bean初期化
	@ModelAttribute("reserveForm")
	public ReserveForm setReserveForm() {
		return new ReserveForm();
	}
	
	//バリエーションを追加
	@InitBinder("reserveForm")
	public void initBinderForReserveForm(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(reserveValidator);
	}
	
	/**
	 * 
	 * reserveメソッド
	 * 
	 * 予約
	 * 
	 * @param bookId
	 * @param reserveDate
	 * @param maxPeriod
	 * @param reserveForm
	 * @param model
	 * @return
	 */
	@PostMapping("user/books/reserve")
	public String reserve(@RequestParam("bookId") Integer bookId,@RequestParam LocalDate reserveDate,@RequestParam Long maxPeriod, ReserveForm reserveForm, Model model) {
		
		//予約情報をフォームに渡す
		reserveForm.setBooksId(bookId);
		reserveForm.setReserveDate(reserveDate);						//選択された予約開始日を設定
		reserveForm.setMaxReturnDate(reserveDate.plusDays(maxPeriod));	//予約開始日から最大貸出可能日を設定
		reserveForm.setMinReturnDate(reserveDate.plusDays(1));			//予約開始日の翌日を最短貸出日に設定
		return "/user/books/reserve/reserve";
	}
	
	/**
	 * 
	 * reservesaveメソッド
	 * 
	 * 予約登録
	 * 
	 * @param reserveForm
	 * @param br
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("user/books/reservesave")
	public String reservesave(@Validated ReserveForm reserveForm, BindingResult br,Model model,RedirectAttributes redirectAttributes) {
		
		//バリデーションエラー時、前のページに戻る
		if(br.hasErrors()) {
			return "/user/books/reserve/reserve";
		}
		
		//ユーザーIDを取得しフォームと共にデータベースに予約情報を登録
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Reserve reserve = reserveService.saveReserve(reserveForm, userId);
		
		//登録した予約情報のIdを渡し、リダイレクト
		redirectAttributes.addAttribute("id", reserve.getReserveId());
		
		return "redirect:/user/books/reserveconf/{id}";
		
	}
	
	/**
	 * 
	 * reserveConfメソッド
	 * 
	 * 予約確認
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/user/books/reserveconf/{id}")
	public String reserveConf(@PathVariable Long id,Model model) {
		
		//ユーザーIDを取得し、予約IDとユーザーIDから予約情報を取得、ビューへ渡す
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Reserve reserve = reserveService.readReserve(id,userId);

		model.addAttribute("reserve",reserve);
		
		return "/user/books/reserve/reserveconf";
		
	}
	
	/**
	 * 
	 * selectDayメソッド
	 * 
	 * 予約日時選択
	 * 
	 * @param bookId
	 * @param model
	 * @return
	 */
	@PostMapping("/user/books/reserve/selectday")
	public String selectDay(@RequestParam("bookId") Integer bookId,Model model) {
		
		//予約可能日時の情報を週・日付の二重リストで取得し貸出予定書籍IDと共にビューへ渡す
		List<List<DayMaxPeriod>> monthList = reserveService.findMaxPeriodList(bookId);
		model.addAttribute("monthlist", monthList);
		model.addAttribute("bookId", bookId);
		return "user/books/reserve/selectday";
	}

}
