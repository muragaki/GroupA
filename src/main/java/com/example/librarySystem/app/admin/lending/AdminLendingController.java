package com.example.librarySystem.app.admin.lending;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.service.LendingService;
import com.example.librarySystem.validator.lending.SearchLendingValidator;


/**
 * 
 * AdminLendingControllerクラス
 * 
 * 管理者貸出コントローラー
 * 
 * @author 中尾 寿晃
 *
 */
@Controller
public class AdminLendingController {
	
	@Autowired
	LendingService lendingService;
	
	@Autowired
	SearchLendingValidator searchLendingValidator;
	
	//form-backing bean初期化
	@ModelAttribute("searchLendingForm")
	public SearchLendingForm setSearchLendingForm() {
		return new SearchLendingForm();
	}
	
	//未入力フィールドをnullに設定
	//バリエーションを追加
	@InitBinder("searchLendingForm")
	public void initBinderForSearchLendingForm(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		webDataBinder.addValidators(searchLendingValidator);
	}

	/**
	 * lensinglistメソッド
	 * 
	 * 貸出一覧
	 * 
	 * @param searchLendingForm
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping("admin/lending")
	public String lendinglist(@Validated SearchLendingForm searchLendingForm, BindingResult bindingResult, Model model) {
		
		//バリデーションエラー時、一覧を取得し前のページ
		if(bindingResult.hasErrors()) {
			List<Lending> lendinglist = lendingService.findAll();
			model.addAttribute("lendinglist", lendinglist);
			return "admin/lending/lendinglist";
		}
		
		//検索条件の一覧を取得しビューへ渡す
		List<Lending> lendinglist = lendingService.findSearch(searchLendingForm);
		model.addAttribute("lendinglist", lendinglist);
	
		return "admin/lending/lendinglist";
	}

}