package com.jewelry.cms.menu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jewelry.cms.menu.domain.MenuAuthTO;
import com.jewelry.cms.menu.domain.MenuAuthVO;

@Mapper
public interface MenuAuthMapper {
		
	List<MenuAuthVO> selectUserAuthMenuList(MenuAuthTO to);

	int insertUserAuthMenu(MenuAuthTO to) throws Exception;
	int insertUserAuthMenus(List<MenuAuthTO> to) throws Exception;

	Integer selectUserAuthMenuExistCnt(MenuAuthTO to);
	
	List<MenuAuthVO> selectUserAuthMenusWithUserId(String userId);
	
	int updateUserAuthMenu(MenuAuthTO to) throws Exception;
	int updateUserAuthMenus(List<MenuAuthTO> to) throws Exception;
	
	List<MenuAuthVO> selectUserAuthMenus(MenuAuthTO to);
}
