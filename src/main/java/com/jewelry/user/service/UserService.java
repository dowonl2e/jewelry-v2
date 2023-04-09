package com.jewelry.user.service;

import com.jewelry.user.domain.UserTO;
import com.jewelry.user.domain.UserVO;

import java.util.Map;

public interface UserService {
	
	Map<String, Object> findAllUser(UserTO to);
	
	String insertUser(UserTO to);
	
	UserVO findUser(String userid);

	UserVO findUserByToken(String accessToken);

	String updateUser(UserTO to);
	
	
}
