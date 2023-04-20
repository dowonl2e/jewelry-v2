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
	
	@GetMapping("/list")
	public Map<String, Object> findAllUser(final UserTO to){
		return userService.findAllUser(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(
			@RequestHeader("Authorization") String accessToken,
			@RequestBody final UserTO to) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		to.setInpt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		to.setUser_pwd(passwordEncoder.encode(to.getUser_pwd()));
		to.setUser_role("MANAGER");
		String result = userService.insertUser(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@GetMapping("/{userid}")
	public UserVO findUser(@PathVariable final String userid) {
		return userService.findUser(userid);
	}

	@PatchMapping("/modify/{userid}")
	public ResponseEntity<Object> modify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final String userid,
			@RequestBody final UserTO to) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		to.setUser_pwd(ObjectUtils.isEmpty(to.getUser_pwd()) ? null : passwordEncoder.encode(to.getUser_pwd()));
		to.setUser_id(userid);
		String result = userService.updateUser(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
}
