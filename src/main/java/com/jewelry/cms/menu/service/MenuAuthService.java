package com.jewelry.cms.menu.service;

import java.util.List;
import java.util.Map;

import com.jewelry.cms.menu.domain.MenuAuthTO;
import com.jewelry.cms.menu.domain.MenuAuthVO;
import com.jewelry.user.domain.UserTO;

public interface MenuAuthService {
		
	Map<String, Object> findAllManager(UserTO to);
	
	Map<String, Object> findAllMenuAuth(MenuAuthTO to);

	String updateMenusAuth(MenuAuthTO to);
	
	String updateMenuAuth(MenuAuthTO to);
	
	List<MenuAuthVO> findUserMenusAuth(MenuAuthTO to);
}
