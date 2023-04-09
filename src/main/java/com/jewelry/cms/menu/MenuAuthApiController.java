package com.jewelry.cms.menu;

import com.jewelry.cms.menu.domain.MenuAuthTO;
import com.jewelry.cms.menu.service.MenuAuthService;
import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.response.ResponseCode;
import com.jewelry.user.domain.UserTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menuauth")
public class MenuAuthApiController {

	private final MenuAuthService authService;

	private final JwtTokenProvider jwtTokenProvider;

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
	public ResponseEntity<Object> writeAuthMenus(
			@RequestHeader("Authorization") String accessToken,
			final MenuAuthTO to) {
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setInpt_id(userId);
		to.setUpdt_id(userId);
		String result = authService.updateMenusAuth(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());

	}
	
	@PostMapping("/menu")
	public ResponseEntity<Object> authMenu(
			@RequestHeader("Authorization") String accessToken,
			final MenuAuthTO to) {
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setInpt_id(userId);
		to.setUpdt_id(userId);
		String result = authService.updateMenuAuth(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());

	}
}
