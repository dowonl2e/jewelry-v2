package com.jewelry.user;

import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.response.ResponseCode;
import com.jewelry.user.domain.UserTO;
import com.jewelry.user.domain.UserVO;
import com.jewelry.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {
	
	private final UserService userService;

	private final JwtTokenProvider jwtTokenProvider;
	private final String menuId = "user";

	@GetMapping("/list")
	public Map<String, Object> findAllUser(
			@RequestHeader("Authorization") String accessToken,
			final UserTO to){
		to.setMenuId(menuId);
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		return userService.findAllUser(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(
			@RequestHeader("Authorization") String accessToken,
			@RequestBody final UserTO to) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setInpt_id(userId);
		to.setUser_pwd(passwordEncoder.encode(to.getUser_pwd()));
		to.setUser_role("MANAGER");
		String result = userService.insertUser(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@GetMapping("/{userid}")
	public UserVO findUser(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final String userid) {
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		UserTO to = new UserTO();
		to.setMenuId(menuId);
		to.setUser_id(userId);
		return userService.findUser(to);
	}

	@PatchMapping("/modify/{userid}")
	public ResponseEntity<Object> modify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable("userid") final String tgt_user_id,
			@RequestBody final UserTO to) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setTgt_user_id(tgt_user_id);
		to.setUser_pwd(ObjectUtils.isEmpty(to.getUser_pwd()) ? null : passwordEncoder.encode(to.getUser_pwd()));
		to.setUpdt_id(userId);
		String result = userService.updateUser(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
}
