package com.jewelry.user.entity;

import com.jewelry.authentication.jwt.values.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="tb_user")
public class UserEntity {
	
	@Id
	@Column(name = "user_id", updatable = false, unique = true, nullable = false)
	private String userId;
	
	@Column(name = "user_pwd")
	private String userPwd;
	
	@Column(name = "user_name")
	private String userName;
	
	private String email;
	
	private String celnum;
	
	private String gender;
	
	@Column(name = "use_yn")
	private String useYn;

	//@OneToMany(mappedBy = "userId")	//조인 대상의 변수명
	@Column(name = "user_role")
	@Enumerated(EnumType.STRING)
	private Role userRole;

	@Builder
	public UserEntity(String userId, String userPwd, String userName, String email, String celnum, String gender, String useYn, Role userRole){
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.email = email;
		this.celnum = celnum;
		this.gender = gender;
		this.useYn = useYn;
		this.userRole = userRole;
	}
}
