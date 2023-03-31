package com.jewelry.user;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelry.response.ResponseCode;
import com.jewelry.user.domain.UserTO;
import com.jewelry.user.domain.UserVO;
import com.jewelry.user.entity.CustomUserDetails;
import com.jewelry.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {
	
	private final UserService userService;
	
	private final HttpSession session;
	
	@GetMapping("/list")
	public Map<String, Object> findAllUser(final UserTO to){
		return userService.findAllUser(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(@RequestBody final UserTO to) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		to.setInpt_id(((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername());
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
	public ResponseEntity<Object> modify(@PathVariable final String userid, @RequestBody final UserTO to) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		to.setUser_pwd(ObjectUtils.isEmpty(to.getUser_pwd()) ? null : passwordEncoder.encode(to.getUser_pwd()));
		to.setUpdt_id(((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername());
		to.setUser_id(userid);
		String result = userService.updateUser(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
}
