package com.example.librarySystem.app.admin.lendlog;

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

import com.example.librarySystem.domain.model.LendLog;
import com.example.librarySystem.domain.service.LendLogService;
import com.example.librarySystem.validator.lendLog.SearchLendLogValidator;

@Controller
public class AdminLendLogController {

	@Autowired
	LendLogService lendLogService;
	
	@Autowired
	SearchLendLogValidator searchLendLogValidator;
	
	//form-backing bean初期化
	@ModelAttribute("searchLendLogForm")
	public SearchLendLogForm setSearchLendLogForm() {
		return new SearchLendLogForm();
	}
	
	//未入力フィールドをnullに設定
	//バリエーションを追加
	@InitBinder("searchLendLogForm")
	public void initBinderForSearchLendLogForm(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		webDataBinder.addValidators(searchLendLogValidator);
	}
	
	/**
	 * lendloglistメソッド
	 * 
	 * 利用履歴一覧
	 * 
	 * @param searchLendLogForm
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping("admin/lendlog")
	public String lendloglist(@Validated SearchLendLogForm searchLendLogForm, BindingResult bindingResult, Model model) {
		
		//バリデーションエラー時、全一覧を取得しビューへ渡す
		if(bindingResult.hasErrors()) {
			List<LendLog> lendloglist = lendLogService.findAll();
			model.addAttribute("lendloglist", lendloglist);
			return "admin/lendlog/lendloglist";
		}
		
		//検索条件の一覧を取得しビューへ渡す
		List<LendLog> lendloglist = lendLogService.findSearch(searchLendLogForm);
		model.addAttribute("lendloglist", lendloglist);
	
		return "admin/lendlog/lendloglist";
	}
}
