package com.jewelry.authentication.jwt;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelry.authentication.jwt.entity.TokenTO;
import com.jewelry.authentication.jwt.entity.TokenVO;
import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.response.BasicResponse;
import com.jewelry.response.ResponseCode;
import com.jewelry.response.TokenResponse;
import com.jewelry.user.domain.UserTO;
import com.jewelry.user.service.impl.CustomUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jauth")
@RequiredArgsConstructor
public class JwtAuthController {

	private final CustomUserService customUserService;
	private final JwtTokenProvider jwtTokenProvider;
	
	@PostMapping("/login")
    public ResponseEntity<BasicResponse<TokenResponse>> login(@RequestBody UserTO user) {
		Optional<TokenVO> resObj = Optional.of(customUserService.login(user.getUser_id(), user.getUser_pwd()));
		return ResponseEntity.ok().body(
				new BasicResponse<>(
						ResponseCode.USER_FIND_SUCCESS,
						new TokenResponse(resObj.get().getGrantType(), resObj.get().getAccessToken())
				)
		);
    }
	
	@PostMapping("/authentication")
    public ResponseEntity<BasicResponse<?>> authentication(HttpServletRequest request) {
//		Optional<TokenVO> resObj = Optional.of(customUserService.authentication(SecurityUtils.resolveToken(request)));
		return ResponseEntity.ok().body(new BasicResponse<>(ResponseCode.AUTH_SUCCESS));
    }
	 
}
