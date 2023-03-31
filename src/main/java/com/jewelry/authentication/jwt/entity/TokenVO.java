package com.jewelry.authentication.jwt.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenVO {
	
	private String grantType;
	private String accessToken;
	private String refreshToken;
	private Long accessTokenExpioresIn;
	private LocalDateTime refreshExpiredDate;
	
}
