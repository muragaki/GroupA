package com.example.librarySystem.app.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * LoginControllerクラス
 * 
 * ログイン
 * 
 * @author 中尾 寿晃
 *
 */
@Controller
public class LoginController {
	
	@GetMapping("login")
	String loginForm() {
		return "login/login";
	}

}
