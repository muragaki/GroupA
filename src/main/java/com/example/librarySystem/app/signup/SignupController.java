package com.example.librarySystem.app.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.librarySystem.domain.model.RoleName;
import com.example.librarySystem.domain.model.User;
import com.example.librarySystem.domain.service.SuperUserDetailsService;
import com.example.librarySystem.validator.signup.SignupValidator;

/**
 * 
 * SignupControllerクラス
 * 
 * ユーザー登録
 * 
 * @author 中尾 寿晃
 *
 */
@Controller
public class SignupController {
	
	@Autowired
	SuperUserDetailsService superUserDetailsService;
	
	@Autowired
	SignupValidator signupValidator;
	
	/**
	 * signupメソッド
	 * 
	 * ユーザー登録ページ
	 * 
	 * @return
	 */
	@GetMapping("signup")
	public String signup() {
		return "/login/signup";
	}
	
	//form-backing bean初期化
	@ModelAttribute("signupForm")
	public SignupForm setSignupForm() {
		return new SignupForm();
	}
	
	//バリエーションを追加
	@InitBinder("signupForm")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(signupValidator);
	}
	
	/**
	 * 
	 * signupconfメソッド
	 * 
	 * 新規ユーザーの登録
	 * 
	 * @param signupForm
	 * @param br
	 * @param model
	 * @return
	 */
	@PostMapping("signup")
	public String signupconf(@Validated SignupForm signupForm, BindingResult br,Model model) {
		
		//バリデーションエラー時、前のページへ戻る
		if(br.hasErrors()) {
			return "/login/signup";
		}
		
		//入力情報をインスタンス化しデータベースへ登録
		User user = new User(signupForm.getUserId()
							,signupForm.getPassword()
							,signupForm.getFirstName()
							,signupForm.getLastName()
							,RoleName.USER);
		
		superUserDetailsService.userregist(user);
		return "redirect:/login";
		
	}
	

}
