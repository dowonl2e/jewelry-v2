package com.jewelry.config.filter;

import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.values.JwtHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	//private final LogoutJwtRedisRepository logoutJwtRedisRepository;

  @Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

    	log.info("RequestUrl : " + request.getRequestURI());
    	if(request.getRequestURI().startsWith("/api")) {
			String token = resolveToken(request);
			if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
				Authentication authentication = jwtTokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.info("Security Context에 '"+authentication.getName()+"' 인증 정보를 저장했습니다.");
			}
			else {
				log.info("유효한 토큰이 없습니다.");
//				return;
			}
    	}
		
		filterChain.doFilter(request, response);
	}

	/**토큰 정보 추출 */
	private String resolveToken(HttpServletRequest request){
			String bearerToken = request.getHeader(JwtHeader.AUTHORITY_TYPE_HEADER.getValue());
			if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtHeader.GRANT_TYPE_PREFIX.getValue())){
					return bearerToken.substring(7);
			}
			return null;
	}
}
