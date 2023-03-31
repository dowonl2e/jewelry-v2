package com.jewelry.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jewelry.user.domain.UserTO;
import com.jewelry.user.domain.UserVO;
import com.jewelry.user.entity.CustomUserDetails;

@Mapper
public interface UserMapper {
	
	CustomUserDetails selectUserWithLogin(String user_id);
	
	int selectUserListCount(UserTO to);
	
	List<UserVO> selectUserList(UserTO to);

	int insertUser(UserTO to) throws Exception;
	
	UserVO selectUser(String userid);

	int updateUser(UserTO to) throws Exception;
	
	int selectManagerListCount(UserTO to);
	
	List<UserVO> selectManagerList(UserTO to);


}
