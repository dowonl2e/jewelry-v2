package com.jewelry.cms.menu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu/auth")
public class MenuAuthPageController {
	
	@GetMapping("/managers")
	public String list() {
		return "cms/menu/auth/manager_list";
	}
	
}
