package com.example.librarySystem.app.admin.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.librarySystem.domain.model.LendLog;
import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.model.Reserve;
import com.example.librarySystem.domain.model.RoleName;
import com.example.librarySystem.domain.model.User;
import com.example.librarySystem.domain.service.LendLogService;
import com.example.librarySystem.domain.service.LendingService;
import com.example.librarySystem.domain.service.ReserveService;
import com.example.librarySystem.domain.service.SuperUserDetailsService;

/**
 * 
 * AdminUserControllerクラス
 * 
 * 管理者 利用者コントローラー
 * 
 * @author 中尾 寿晃
 *
 */
@Controller
public class AdminUserController {
	
	@Autowired
	SuperUserDetailsService superUserDetailsService;
	
	@Autowired
	LendingService lendingService;
	
	@Autowired
	LendLogService lendLogService;
	
	@Autowired
	ReserveService reserveService;
	
	/**
	 * 
	 * userListメソッド
	 * 利用者一覧
	 * @param model
	 * @return
	 */
	@RequestMapping("admin/user")
	String userlist(Model model) {
		List<User> userlist = superUserDetailsService.getUserAll();
		model.addAttribute("userlist", userlist);
		return "admin/user/userlist";
	}
	
	/**
	 * userdetailメソッド
	 * 
	 * 利用者詳細
	 * 
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping("admin/user/userdetail")
	String userdetail(@RequestParam String userId, Model model) {
		List<LendLog> lendLoglist = lendLogService.findUserList(userId);
		List<Lending> lendinglist = lendingService.findUserList(userId);
		List<Reserve> reservelist = reserveService.findUserList(userId);
		User user = superUserDetailsService.findById(userId);
		
		model.addAttribute("lendLoglist", lendLoglist);
		model.addAttribute("reservelist", reservelist);
		model.addAttribute("lendinglist", lendinglist);
	
		model.addAttribute("user", user);
		return "admin/user/userdetail";
		
	}
	
	/**
	 * usereditメソッド
	 * 
	 * 利用者情報編集
	 * 
	 * @param userId
	 * @param userForm
	 * @param model
	 * @return
	 */
	@RequestMapping("admin/user/useredit")
	String useredit(@RequestParam String userId,UserForm userForm, Model model) {
		User user = superUserDetailsService.findById(userId);
		userForm.setUsername(user.getUserId());
		userForm.setFirstname(user.getFirstName());
		userForm.setLastname(user.getLastName());
		userForm.setRolename(user.getRoleName());
		userForm.setRoleNameList(new ArrayList<RoleName>(Arrays.asList(RoleName.ADMIN,RoleName.USER)));
		model.addAttribute("userForm", userForm);
		
		return "admin/user/useredit";
				
	}
	
	/**
	 * editconfメソッド
	 * 
	 * 利用者情報更新
	 * 
	 * @param userForm
	 * @param br
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@PostMapping("admin/user/editconf")
	String editconf(@ModelAttribute("userForm") @Validated UserForm userForm, BindingResult br, RedirectAttributes redirectAttributes, Model model) {
		
		if (br.hasErrors()) {
			model.addAttribute("userForm", userForm);
			return "admin/user/useredit";
		}
		
		//更新がある情報を書き換えてデータベースを更新
		User user = superUserDetailsService.findById(userForm.getUsername());
		if (!user.getFirstName().equals(userForm.getFirstname())) {
			user.setFirstName(userForm.getFirstname());
		}
		if (!user.getLastName().equals(userForm.getLastname())) {
			user.setLastName(userForm.getLastname());
		}
		if (!user.getRoleName().getValue().equals(userForm.getRolename().getValue())) {
			user.setRoleName(userForm.getRolename());
		}
		superUserDetailsService.userRewrite(user);
		
		//ユーザーIDを渡してリダイレクト
		redirectAttributes.addAttribute("userId", userForm.getUsername());
		
		return "redirect:/admin/user/userdetail";
	}

	
}
