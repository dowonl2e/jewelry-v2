package com.jewelry.cms.menu;

import com.jewelry.cms.menu.domain.MenuAuthTO;
import com.jewelry.cms.menu.domain.MenuAuthVO;
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

	private final MenuAuthService menuAuthService;

	private final JwtTokenProvider jwtTokenProvider;

	private final String menuId = "menu/auth";

	@GetMapping("/managers")
	public Map<String, Object> managers(
			@RequestHeader("Authorization") String accessToken,
			final UserTO to){
		to.setMenuId(menuId);
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		return menuAuthService.findAllManager(to);
	}

	@GetMapping("/menus/{userId}")
	public Map<String, Object> menusByUserId(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable("userId") final String tgt_user_id,
			final MenuAuthTO to){
		to.setMenuId(menuId);
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		to.setTgt_user_id(tgt_user_id);
		return menuAuthService.findAllMenuAuth(to);
	}

	@PostMapping("/menus")
	public ResponseEntity<Object> writeAuthMenus(
			@RequestHeader("Authorization") String accessToken,
			final MenuAuthTO to) {
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setInpt_id(userId);
		to.setUpdt_id(userId);
		String result = menuAuthService.updateMenusAuth(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PostMapping("/menu")
	public ResponseEntity<Object> authMenu(
			@RequestHeader("Authorization") String accessToken,
			final MenuAuthTO to) {
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setInpt_id(userId);
		to.setUpdt_id(userId);
		String result = menuAuthService.updateMenuAuth(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PostMapping("/user/auth")
	public MenuAuthVO userAuth(
			@RequestHeader("Authorization") String accessToken,
			@RequestBody final MenuAuthTO to){
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		return menuAuthService.findUserMenuAuth(to);
	}
}
