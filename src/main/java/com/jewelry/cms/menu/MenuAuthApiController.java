package com.jewelry.cms.menu;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelry.cms.menu.domain.MenuAuthTO;
import com.jewelry.cms.menu.service.MenuAuthService;
import com.jewelry.response.ResponseCode;
import com.jewelry.user.domain.UserTO;
import com.jewelry.user.entity.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menuauth")
public class MenuAuthApiController {
	
	private final HttpSession session;

	private final MenuAuthService authService;
	
	@GetMapping("/managers")
	public Map<String, Object> managers(final UserTO to){
		return authService.findAllManager(to);
	}
	
	@GetMapping("/menus/{userId}")
	public Map<String, Object> menusByUserId(@PathVariable final String userId, final MenuAuthTO to){
		to.setUser_id(userId);
		return authService.findAllMenuAuth(to);
	}
	
	@PostMapping("/menus")
	public ResponseEntity<Object> writeAuthMenus(final MenuAuthTO to) {
		String userId = ((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername();
		to.setInpt_id(userId);
		to.setUpdt_id(userId);
		String result = authService.updateMenusAuth(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return  new ResponseEntity<>(response.getStatus());

	}
	
	@PostMapping("/menu")
	public ResponseEntity<Object> authMenu(final MenuAuthTO to) {
		String userId = ((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername();
		to.setInpt_id(userId);
		to.setUpdt_id(userId);
		String result = authService.updateMenuAuth(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return  new ResponseEntity<>(response.getStatus());

	}
}
