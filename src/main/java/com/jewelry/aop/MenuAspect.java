package com.jewelry.aop;

import com.jewelry.cms.menu.service.MenuService;
import com.jewelry.config.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
//@Aspect
//@Component
@RequiredArgsConstructor
public class MenuAspect {

	private final MenuService menuService;
	private final JwtTokenProvider jwtTokenProvider;
	
	@Pointcut(
		"(" +
			"execution(* com.jewelry..*PageController.*(..)) || " +
			"execution(* com.jewelry.cms..*PageController.*(..))" +
		") && " + 
//		"!execution(* com.jewelry.*.UserController.*(..)) && " +
//		"!execution(* com.jewelry.*.FileController.*(..)) && " +
//		"!execution(* com.jewelry.ErrorController.*(..)) && " +
//		"!execution(* com.jewelry.*.*ApiController.*(..)) && " +
//		"!execution(* com.jewelry.cms.*.*ApiController.*(..)) && " +
		"!execution(* com.jewelry..*PageController.*Popup(..)) && " + 
		"!execution(* com.jewelry.cms..*PageController.*Popup(..)) "
	)
	private void menuPointCut() {}
	
	//메뉴
	@Before("menuPointCut()")
	public void beforeAdvice(JoinPoint joinPoint) throws Exception {
		log.info("[****************************** 메뉴 조회 AOP ***********************************");
		
		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		/*
        //토큰에서 사용자 아이디 및 권한 조회
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = SecurityUtils.getCurrentMemberId();
		String userRole = SecurityUtils.hasRole("ROLE_ADMIN") ? "ADMIN" : (SecurityUtils.hasRole("ROLE_MANAGER") ? "MANAGER" : "NONE");
		
		MenuTO menuto = new MenuTO();
		menuto.setUse_yn("Y");
		menuto.setMenu_depth(1);
		if(userRole.equals("ADMIN")) {
			request.setAttribute("menus", menuService.selectMenuListAll(menuto));
			menuto.setMenu_depth(2);
			request.setAttribute("submenus", menuService.selectMenuListAll(menuto));
		}
		else {
			menuto.setUser_id(userId);
			request.setAttribute("menus", menuService.selectMenuList(menuto));
			menuto.setMenu_depth(2);
			request.setAttribute("submenus", menuService.selectMenuList(menuto));
		}
		*/
	}
}
