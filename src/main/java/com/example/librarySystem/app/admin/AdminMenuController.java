package com.example.librarySystem.app.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * AdminMenuControllerメソッド
 * 
 * 管理者メニュー
 * 
 * @author 中尾 寿晃
 *
 */
@Controller
public class AdminMenuController {
	
	@GetMapping("admin/menu")
	public String adminMenu() {
		return "/admin/menu";
	}

}
