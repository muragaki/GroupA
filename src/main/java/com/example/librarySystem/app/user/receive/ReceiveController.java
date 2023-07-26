package com.example.librarySystem.app.user.receive;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.librarySystem.domain.model.ColBooks;
import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.model.Reserve;
import com.example.librarySystem.domain.model.SituationName;
import com.example.librarySystem.domain.service.ColBooksService;
import com.example.librarySystem.domain.service.LendingService;
import com.example.librarySystem.domain.service.ReserveService;
import com.example.librarySystem.domain.service.SuperUserDetails;
import com.example.librarySystem.validator.receive.ReserveEditValidator;

/**
 * 
 * ReceiveControllerクラス
 * 
 * 予約受け取り
 * 
 * @author 中尾 寿晃
 *
 */
@Controller
public class ReceiveController {
	
	@Autowired
	ReserveService reserveService;
	
	@Autowired
	ColBooksService colBooksService;
	
	@Autowired
	LendingService lendingService;
	
	@Autowired
	ReserveEditValidator reserveEditValidator;
	
	//form-backing bean初期化
	@ModelAttribute("reserveEditForm")
	public ReserveEditForm setReserveEditForm() {
		return new ReserveEditForm();
	}
	
	//バリエーションを追加
	@InitBinder("reserveEditForm")
	public void initBinderForReserveEditForm(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(reserveEditValidator);
	}
	
	/**
	 * receveメソッド
	 * 
	 * 予約一覧
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("user/receive")
	public String receive(Model model) {
		
		//ユーザーIDの取得
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		
		//当日予約リスト、及び翌日以降の予約リストをそれぞれ取得しビューへ渡す
		List<Reserve> toDayReserveList = reserveService.findDayReserveList(userId, LocalDate.now());
		List<Reserve> afterDayReserveList = reserveService.findAfterDayReserveList(userId, LocalDate.now());
		
		model.addAttribute("toDayReserveList", toDayReserveList);
		model.addAttribute("afterDayReserveList", afterDayReserveList);
		
		return "/user/receive/receive";
	}
	
	/**
	 * 
	 * recevingメソッド
	 * 
	 * 予約受け取り
	 * 
	 * @param reserveId
	 * @param model
	 * @return
	 */
	@PostMapping("user/receive/receiving")
	public String receiving(@RequestParam Long reserveId,Model model) {
		
		//ユーザーIDと選択予約情報を取得
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Reserve reserve = reserveService.readReserve(reserveId, userId);
		
		boolean notbook = true; //受け渡し判定用
		
		//受け渡し予定の蔵書情報を取得
		ColBooks colBooks = colBooksService.readColBooksId(reserve.getColBooksId());
			
		//該当蔵書が受け渡し可能か判定
		if(colBooks.getSituationName() != SituationName.AVAILABLE) {
			
			//現在貸出可能な蔵書の有無を判定
			if(colBooksService.findLendCheck(reserve.getColBooks().getBooksId())) {
				
				//貸出可能な蔵書の中から予約期間貸し出せる蔵書のIDを取得
				Long colBooksId = reserveService.getReserveColBooksId(reserve.getColBooks().getBooksId(), reserve.getReserveDate(), reserve.getScheduledReturnDate(), reserveId);
				
				if(colBooksId == -1) {						//無ければ受け渡し判定をfalseに
					notbook = false;
				}else {										//有れば受け渡し蔵書IDを設定
					reserve.setColBooksId(colBooksId);
				}
				
			}else {					//貸出可能な蔵書が無ければ受け渡し判定をfalseに
				notbook = false;
			}
			
		}
		
		model.addAttribute("notbook", notbook);
		model.addAttribute("reserve", reserve);
		
		return "/user/receive/receiving";
		
	}
	
	/**
	 * 
	 * receivesaveメソッド
	 * 
	 * 受け渡し処理
	 * 
	 * @param receiveForm
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("user/receive/receivesave")
	public String receivesave(ReceiveForm receiveForm ,RedirectAttributes redirectAttributes) {
		
		//ユーザーIDを取得し、受け渡し情報から貸出情報をインスタンス化しデータベースへ登録
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Lending lending = new Lending(userId, receiveForm.getColBooksId(), LocalDateTime.now(), receiveForm.getScheduledReturnDate());
		lending=lendingService.saveLend(lending);
		
		//貸し出した蔵書の状態を貸出中に更新
		ColBooks colBooks = colBooksService.readColBooksId(lending.getColBooksId());
		colBooks.setSituationName(SituationName.LENDING);
		colBooksService.saveColBooks(colBooks);
		
		//貸し出した予約内容を削除
		reserveService.deleteReserve(receiveForm.getReserveId());
		
		//登録した貸出情報を渡してリダイレクト
		redirectAttributes.addAttribute("lending", lending);
		return "redirect:/user/receive/receiveconf";
		
	}
	
	/**
	 * 
	 * receiveconfメソッド
	 * 
	 * 受け渡し確定
	 * 
	 * @param lending
	 * @param model
	 * @return
	 */
	@RequestMapping("user/receive/receiveconf")
	public String receiveconf(@RequestParam Lending lending,Model model) {
		model.addAttribute("lending", lending);
		return "user/receive/receiveconf";
	}
	
	/**
	 * 
	 * reserveDetail
	 * 
	 * 予約情報詳細
	 * 
	 * @param reserveId
	 * @param model
	 * @return
	 */
	@RequestMapping("user/receive/reservedetail")
	public String reserveDetail(@RequestParam Long reserveId,Model model) {
		
		//ユーザーIDを取得しユーザーIDから予約内容の詳細情報を取得、ビューへ渡す
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Reserve reserve = reserveService.readReserve(reserveId, userId);
		
		model.addAttribute("reserve", reserve);
		return "user/receive/reservedetail";
	
	}
	
	/**
	 * 
	 * reseeveeditメソッド
	 * 
	 * 予約内容の編集
	 * 
	 * @param reserveId
	 * @param reserveEditForm
	 * @param model
	 * @return
	 */
	@PostMapping("user/receive/reserveedit")
	public String reserveedit(@RequestParam Long reserveId,ReserveEditForm reserveEditForm, Model model) {
		
		//ユーザーIDを取得し予約情報を取得
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Reserve reserve = reserveService.readReserve(reserveId, userId);
		
		//予約情報をフォームに渡す
		reserveEditForm.setReserveId(reserveId);
		reserveEditForm.setBooksId(reserve.getColBooks().getBooksId());
		reserveEditForm.setReserveDate(reserve.getReserveDate());
		reserveEditForm.setScheduledReturnDate(reserve.getScheduledReturnDate());
		
		return "user/receive/reserveedit";
	}
	
	/**
	 * 
	 * reserveReconf
	 * 
	 * 予約情報の再更新
	 * 
	 * @param reserveEditForm
	 * @param bindingResult
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("user/receive/reservereconf")
	public String reserveReconf(@Validated ReserveEditForm reserveEditForm, BindingResult bindingResult,RedirectAttributes redirectAttributes) {
		
		//バリデーションエラー時、前のページに戻る
		if(bindingResult.hasErrors()) {
			return "user/receive/reserveedit";
		}
		
		//ユーザーIDを取得し、更新予定の予約情報を取得
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		Reserve reserve = reserveService.readReserve(reserveEditForm.getReserveId(), userId);
		
		//予約内容を上書きし、データベースを更新
		reserve.setColBooksId(reserveService.getReserveColBooksId(reserveEditForm.getBooksId(), reserveEditForm.getReserveDate(), reserveEditForm.getScheduledReturnDate(),reserve.getReserveId()));
		reserve.setReserveDate(reserveEditForm.getReserveDate());
		reserve.setScheduledReturnDate(reserveEditForm.getScheduledReturnDate());
		
		reserveService.saveReserve(reserve);
		
		//更新した予約情報のIDを渡してリダイレクト
		redirectAttributes.addAttribute("reserveId", reserveEditForm.getReserveId());
		return "redirect:/user/receive/reservedetail";
		
	}
	
	/**
	 * 
	 * reserveDeleteメソッド
	 * 
	 * 予約削除
	 * 
	 * @param reserveId
	 * @return
	 */
	@PostMapping("/user/receive/reservedelete")
	public String reserveDelete(@RequestParam Long reserveId) {
		
		//予約情報をデータベースから削除しリダイレクト
		reserveService.deleteReserve(reserveId);
		return "redirect:/user/receive";
	}
	
}
