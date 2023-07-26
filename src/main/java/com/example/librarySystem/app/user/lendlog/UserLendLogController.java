package com.example.librarySystem.app.user.lendlog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.librarySystem.domain.model.LendLog;
import com.example.librarySystem.domain.service.LendLogService;
import com.example.librarySystem.domain.service.SuperUserDetails;
import com.example.librarySystem.domain.service.SuperUserDetailsService;

/**
 * 
 * UserLendLogControllerクラス
 * 
 * 利用者 利用履歴
 * 
 * @author 中尾 寿晃
 *
 */
@Controller
public class UserLendLogController {
	
	@Autowired
	SuperUserDetailsService superUserDetailsService;
	
	@Autowired
	LendLogService lendLogService;
	
	/**
	 * 
	 * lendlogメソッド
	 * 
	 * 利用者 利用履歴一覧
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("user/lendlog")
	public String lendlog(Model model) {
		
		//利用中の利用者Idを取得
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId();
		
		//利用者IDから利用履歴をリストで取得しビューへ渡す
		List<LendLog> lendloglist = lendLogService.findUserList(userId);
		
		model.addAttribute("lendloglist",lendloglist);
		
		return "/user/lendlog/lendlog";
	}

}
