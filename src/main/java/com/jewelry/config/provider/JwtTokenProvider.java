package com.jewelry.config.provider;

import java.security.Key;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.amazonaws.util.CollectionUtils;
import com.jewelry.authentication.jwt.entity.TokenVO;
import com.jewelry.exception.CustomException;
import com.jewelry.response.ResponseCode;
import com.jewelry.values.JwtHeader;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {
	
	private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; //7일
	
    private Key key;
    
    public JwtTokenProvider(@Value("${jwt.secret}") String secret){
		byte[] keyBytes = Base64.getDecoder().decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * token 생성 algorithm 
     * 
     */
    public TokenVO authentication(Authentication authentication){
    	//권한 가져오기
    	String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		Date now = new Date();
		
		Date accessTokenExpiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
		//AccessToken 생성
		String accessToken = Jwts.builder()
				.setSubject(authentication.getName()) 		// payload "sub": "name"
				.setIssuedAt(now)
				.claim(JwtHeader.AUTHORITY_TYPE_HEADER.getValue(), authorities)		// payload "Authorization": "ROLE_ADMIN" OR "ROLE_MANAGER"
				.setExpiration(accessTokenExpiresIn)		// payload "exp": 1516239022 (예시)
				.signWith(key, SignatureAlgorithm.HS512)	// header "alg": "HS512"
				.compact();
		
		
		Date refreshTokenExpiredIn = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);
		String refreshToken = Jwts.builder()
				.setExpiration(refreshTokenExpiredIn)
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
		
		return TokenVO.builder()
				.grantType(JwtHeader.GRANT_TYPE_PREFIX.getValue())
				.accessToken(accessToken)
				.accessTokenExpioresIn(accessTokenExpiresIn.getTime())
				.refreshToken(refreshToken)
				.refreshExpiredDate(refreshTokenExpiredIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
				.build();
    }
    
    /**
     * token 생성 algorithm 
     * 
     */
    public TokenVO generateToken(UserDetails userDetails){
    	//권한 가져오기
    	String authorities = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		Date now = new Date();
		
		Date accessTokenExpiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
		//AccessToken 생성
		String accessToken = Jwts.builder()
				.setSubject(userDetails.getUsername()) 		// payload "sub": "name"
				.setIssuedAt(now)
				.claim(JwtHeader.AUTHORITY_TYPE_HEADER.getValue(), authorities)		// payload "Authorization": "ROLE_ADMIN" OR "ROLE_MANAGER"
				.setExpiration(accessTokenExpiresIn)		// payload "exp": 1516239022 (예시)
				.signWith(key, SignatureAlgorithm.HS512)	// header "alg": "HS512"
				.compact();
		
		
		Date refreshTokenExpiredIn = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);
		String refreshToken = Jwts.builder()
				.setExpiration(refreshTokenExpiredIn)
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
		
		return TokenVO.builder()
				.grantType(JwtHeader.GRANT_TYPE_PREFIX.getValue())
				.accessToken(accessToken)
				.accessTokenExpioresIn(accessTokenExpiresIn.getTime())
				.refreshToken(refreshToken)
				.refreshExpiredDate(refreshTokenExpiredIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
				.build();
    }
    
    /**
	 * 토큰을 복호화하여 토큰에 들어있는 정보를 가져온다.
	 * AccessToken에만 유저 정보를 담고 있다.
	 * SecurityContext가 Authentication를 지니고 있기에 UserDetails는 SecurityContext를 사용하기 위함이다.
	 * @param accessToken
	 * @return
	 */
	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);
		
		if(claims.get(JwtHeader.AUTHORITY_TYPE_HEADER.getValue()) == null) {
			throw new CustomException(ResponseCode.NOT_EXIST_AUTH_TOKEN);
		}

		Collection<? extends GrantedAuthority> authorities = 
				Arrays.stream(claims.get(JwtHeader.AUTHORITY_TYPE_HEADER.getValue()).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		
		UserDetails principal = new User(claims.getSubject(), "", authorities);
		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
		
	}

    /**token 유효성 검증 */
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("잘못된 JWT 서명입니다.");
        }catch(ExpiredJwtException e){
        	log.info("만료된 JWT 토큰입니다.");
        }catch(UnsupportedJwtException e){
        	log.info("지원하지 않는 JWT 토큰입니다.");
        }catch(IllegalArgumentException e){
        	log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
    
    /**
	 * 토큰을 복호화 한다.
	 * @param accessToken
	 * @return
	 */
	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

    /**
     * 복호화된 토큰에서 name 추출한다.
     * @param accessToken
     * @return
     */
    public String getSubject(String accessToken) {
		return parseClaims(accessToken).getSubject();
    }
    
    /**
     * 복호화된 토큰에서 role 추출한다.
     * @param accessToken
     * @return
     */
    public String getAuthorities(String accessToken) {
    	Collection<? extends GrantedAuthority> authorities = getAuthentication(accessToken).getAuthorities();
    	boolean isAdmin = authorities.stream().filter(o -> o.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
    	if(isAdmin)
    		return "ADMIN";
    	else {
    		List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    		return CollectionUtils.isNullOrEmpty(roles) ? "NONE" : roles.get(0);
    	}
    }
    
    public long getRemainMilliSeconds(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return expiration.getTime() - new Date().getTime();
    }
    
}
