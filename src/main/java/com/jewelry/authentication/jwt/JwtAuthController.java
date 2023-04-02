package com.jewelry.authentication.jwt;

import com.jewelry.authentication.jwt.entity.JwtAuthService;
import com.jewelry.authentication.jwt.entity.TokenVO;
import com.jewelry.response.BasicResponse;
import com.jewelry.response.ResponseCode;
import com.jewelry.response.TokenResponse;
import com.jewelry.user.domain.UserTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/jauth")
@RequiredArgsConstructor
public class JwtAuthController {

	private final JwtAuthService jwtAuthService;

	private final long COOKIE_EXPIRATION = 7776000; // 90일

	@PostMapping("/login")
    public ResponseEntity<BasicResponse<TokenResponse>> login(@RequestBody UserTO user) {
		Optional<TokenVO> resObj = Optional.of(jwtAuthService.login(user.getUser_id(), user.getUser_pwd()));
		// RT 저장
		HttpCookie httpCookie = ResponseCookie.from("refresh-token", resObj.get().getRefreshToken())
				.maxAge(COOKIE_EXPIRATION)
				.httpOnly(true)
				.secure(true)
				.build();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, httpCookie.toString())
				.body(
					new BasicResponse<>(
							ResponseCode.USER_FIND_SUCCESS,
							new TokenResponse(resObj.get().getGrantType(), resObj.get().getAccessToken())
					)
		);
	}

	@PostMapping("/validate")
	public ResponseEntity<?> validate(@RequestHeader("Authorization") String accessToken) {
		if (!jwtAuthService.validate(accessToken)) {
			return ResponseEntity.status(HttpStatus.OK).build(); // 재발급 필요X
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 재발급 필요
		}
	}

	// 토큰 재발급
	@PostMapping("/reissue")
	public ResponseEntity<?> reissue(@CookieValue(name = "refresh-token") String requestRefreshToken,
																	 @RequestHeader("Authorization") String requestAccessToken) {
		TokenVO reissuedTokenVo = jwtAuthService.reIssue(requestAccessToken, requestRefreshToken);

		if (reissuedTokenVo != null) { // 토큰 재발급 성공
			// RT 저장
			ResponseCookie responseCookie = ResponseCookie.from("refresh-token", reissuedTokenVo.getRefreshToken())
					.maxAge(COOKIE_EXPIRATION)
					.httpOnly(true)
					.secure(true)
					.build();
			return ResponseEntity
					.status(HttpStatus.OK)
					.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
					// AT 저장
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + reissuedTokenVo.getAccessToken())
					.build();

		} else { // Refresh Token 탈취 가능성
			// Cookie 삭제 후 재로그인 유도
			ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
					.maxAge(0)
					.path("/")
					.build();
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
					.build();
		}
	}

	// 로그아웃
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken) {
		jwtAuthService.logout(accessToken);
		ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
				.maxAge(0)
				.path("/")
				.build();

		return ResponseEntity
				.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.build();
	}

}
