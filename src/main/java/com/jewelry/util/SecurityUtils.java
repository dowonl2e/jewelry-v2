package com.jewelry.util;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

public class SecurityUtils {
	
	public static boolean hasRole(Collection<? extends GrantedAuthority> authorities, String userRole) {
		
		if(authorities == null || ObjectUtils.isEmpty(userRole))
			return false;
		
        return authorities.stream().filter(o -> o.getAuthority().equals(userRole)).findAny().isPresent();
    }
	
	public static String getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        return authentication.getName();
    }
	
	public static String resolveToken(String accessToken) {
		String token = "";
		try {
			token = accessToken == null || accessToken.length() < 7 ? "" : accessToken.substring(7);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}
}
