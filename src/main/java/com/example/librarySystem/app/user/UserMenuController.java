package com.example.librarySystem.app.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * UserMenuControllerクラス
 * 
 * 利用者メニュー
 * 
 * @author 中尾 寿晃
 *
 */
@Controller
public class UserMenuController {
	
	/**
	 * 
	 * userMenuメソッド
	 * 
	 * 利用者メニュー
	 * 
	 * @return
	 */
	@GetMapping("/user/menu")
	public String userMenu() {
		return "/user/menu";
	}

}
