package com.jewelry.cms.menu;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelry.cms.menu.domain.MenuTO;
import com.jewelry.cms.menu.domain.MenuVO;
import com.jewelry.cms.menu.service.MenuService;
import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.response.BasicResponse;
import com.jewelry.response.ResponseCode;
import com.jewelry.values.JwtHeader;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class MenuApiController {
	
	private final JwtTokenProvider jwtTokenProvider;
	
	private final MenuService menuService;

	@GetMapping("/user/menus")
	public ResponseEntity<BasicResponse<MenuVO>> userMenus(HttpServletRequest request) {
		
		String bearerToken = request.getHeader(JwtHeader.AUTHORITY_TYPE_HEADER.getValue());
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtHeader.GRANT_TYPE_PREFIX.getValue()))
        	bearerToken = bearerToken.substring(7);

        if(bearerToken == null) 
        	return ResponseEntity.ok().body(new BasicResponse<>(ResponseCode.UNAUTHORIZED, null));
        
        String userId = jwtTokenProvider.getSubject(bearerToken);
        String userRole = jwtTokenProvider.getAuthorities(bearerToken);

		MenuTO to = new MenuTO();
		MenuVO vo = new MenuVO();
		to.setUse_yn("Y");
		to.setMenu_depth(1);
		if(userRole.equals("ADMIN")) {
			vo.setList(menuService.selectMenuListAll(to));
			to.setMenu_depth(2);
			vo.setSubList(menuService.selectMenuListAll(to));
		}
		else {
			to.setUser_id(userId);
			vo.setList(menuService.selectMenuList(to));
			to.setMenu_depth(2);
			vo.setSubList(menuService.selectMenuList(to));
		}
		return ResponseEntity.ok().body(new BasicResponse<>(ResponseCode.AUTH_SUCCESS, vo));
	}
}
