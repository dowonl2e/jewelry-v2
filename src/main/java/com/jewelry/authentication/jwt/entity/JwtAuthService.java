package com.jewelry.authentication.jwt.entity;

import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.config.redis.service.RedisService;
import com.jewelry.response.ResponseCode;
import com.jewelry.user.entity.CustomUserDetails;
import com.jewelry.user.entity.UserEntity;
import com.jewelry.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JwtAuthService {

  private final UserRepository userRepository;
  //private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisService redisService;
  private final String REDIS_REFRESH_TOKEN_SERVER = "JEWELRY_REFRESH_TOKEN";

  @Transactional
  public TokenVO login(String id, String password) {

    UserDetails userDetails = userRepository.findByUserId(id)
        .map(this::createUserDetails)
        .orElseThrow(() -> new UsernameNotFoundException(ResponseCode.USER_INVALID.getMessage()));

    if(!passwordEncoder.matches(password, userDetails.getPassword())) {
      throw new UsernameNotFoundException(ResponseCode.USER_INVALID.getMessage());
    }

    TokenVO tokenVo = jwtTokenProvider.generateToken(userDetails);
    saveRefreshToken(id, tokenVo.getRefreshToken(), tokenVo.getRefreshExpiredDate());

//    Optional<RefreshToken> refreshTokenEntity = refreshTokenRepository.findByKeyId(userDetails.getUsername());
    //refreshToken 정보 여부 및 만료여부 체크
//    if(refreshTokenEntity.isEmpty()) {
//      RefreshToken refreshToken = RefreshToken.builder()
//          .keyId(userDetails.getUsername())
//          .tokenValue(tokenVo.getRefreshToken())
//          .expiredDt(tokenVo.getRefreshExpiredDate())
//          .build();
//
//      refreshTokenRepository.save(refreshToken);
//    }
//    else {
//      //Refresh Token 만료 체크
//      if(jwtTokenProvider.validateToken(refreshTokenEntity.get().getTokenValue()) == false)
//        refreshTokenEntity.get().update(tokenVo.getRefreshToken(), tokenVo.getRefreshExpiredDate());
//    }

    // 3. 인증 정보를 기반으로 JWT 토큰 생성한 토큰 Context에 저장
    Authentication authentication = jwtTokenProvider.getAuthentication(tokenVo.getAccessToken());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return tokenVo;
  }

  // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
  private UserDetails createUserDetails(UserEntity user) {
    return new CustomUserDetails(user);
  }


  // AccessToken이 만료일자만 초과한 유효한 토큰인지 검사
  public boolean validate(String accessTokenInHeader) {
    String accessToken = jwtTokenProvider.resolveToken(accessTokenInHeader);
    return jwtTokenProvider.validateAccessTokenIsExpired(accessToken); // true = 재발급
  }

  // 토큰 재발급: validate 메서드가 true 반환할 때만 사용 -> AccessToken, RefreshToken 재발급
  @Transactional
  public TokenVO reIssue(String accessToken, String refreshToken) {
    String resolveAccessToken = jwtTokenProvider.resolveToken(accessToken);
    Authentication authentication = jwtTokenProvider.getAuthentication(resolveAccessToken);
    String principal = jwtTokenProvider.getPrincipal(resolveAccessToken);

    String refreshTokenInRedis = redisService.getValues(REDIS_REFRESH_TOKEN_SERVER+":" + principal);
    if (ObjectUtils.isEmpty(refreshTokenInRedis)) { // Redis에 저장되어 있는 RefreshToken이 없을 경우
      return null; // -> 재로그인 요청
    }

    // 요청된 RefreshToken의 유효성 검사 & Redis에 저장되어 있는 Ref크reshToken의 같은지 비교
    if(!refreshTokenInRedis.equals(refreshToken)
        || !jwtTokenProvider.validateRefreshToken(refreshToken)) {
      redisService.deleteValues(REDIS_REFRESH_TOKEN_SERVER+":" + principal); // 탈취 가능성 -> 삭제
      return null; // -> 재로그인 요청
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String authorities = jwtTokenProvider.getAuthorities(authentication);

    // 토큰 재발급 및 Redis 업데이트
    redisService.deleteValues(REDIS_REFRESH_TOKEN_SERVER+":" + principal); // 기존 RT 삭제
    TokenVO tokenVo = jwtTokenProvider.generateToken(principal, authorities);
    saveRefreshToken(principal, tokenVo.getRefreshToken(), tokenVo.getRefreshExpiredDate());
    return tokenVo;
  }

  // 토큰 발급
  @Transactional
  public TokenVO generateToken(String principal, String authorities) {
    // RefreshToken이 이미 있을 경우
    if(redisService.getValues(REDIS_REFRESH_TOKEN_SERVER+":" + principal) != null) {
      redisService.deleteValues(REDIS_REFRESH_TOKEN_SERVER+":" + principal); // 삭제
    }

    // AT, RT 생성 및 Redis에 RT 저장
    TokenVO tokenVo = jwtTokenProvider.generateToken(principal, authorities);
    saveRefreshToken(principal, tokenVo.getRefreshToken(), tokenVo.getRefreshExpiredDate());
    return tokenVo;
  }

  // RT를 Redis에 저장
  @Transactional
  public void saveRefreshToken(String principal, String refreshToken, LocalDateTime refreshExpiredDate) {
    redisService.setValuesWithTimeout(REDIS_REFRESH_TOKEN_SERVER+":" + principal, // key
        refreshToken, // value
        jwtTokenProvider.getTokenExpirationTime(refreshToken)); // timeout(milliseconds)

//    Optional<RefreshToken> refreshTokenEntity = refreshTokenRepository.findByKeyId(principal);

    //RefreshToken 재발급으로 인한 갱신토큰 갱신
//    if(refreshTokenEntity.isEmpty()) {
//      RefreshToken refreshTokenObj = RefreshToken.builder()
//          .keyId(principal)
//          .tokenValue(refreshToken)
//          .expiredDt(refreshExpiredDate)
//          .build();
//      refreshTokenRepository.save(refreshTokenObj);
//    }
//    else {
//      if(jwtTokenProvider.validateToken(refreshTokenEntity.get().getTokenValue()) == false
//          || !ObjectUtils.nullSafeEquals(refreshToken, refreshTokenEntity.get().getTokenValue()))
//        refreshTokenEntity.get().update(refreshToken, refreshExpiredDate);
//    }
  }

  // 로그아웃
  @Transactional
  public void logout(String accessToken) {
    String resolveAccessToken = jwtTokenProvider.resolveToken(accessToken);
    String principal = jwtTokenProvider.getPrincipal(resolveAccessToken);

    // Redis에 저장되어 있는 RT 삭제
    String refreshTokenInRedis = redisService.getValues(REDIS_REFRESH_TOKEN_SERVER+":" + principal);
    if (!ObjectUtils.isEmpty(refreshTokenInRedis)) {
      redisService.deleteValues(REDIS_REFRESH_TOKEN_SERVER+":" + principal);
    }

    // Redis에 로그아웃 처리한 AT 저장
    long expiration = jwtTokenProvider.getTokenExpirationTime(resolveAccessToken) - new Date().getTime();
    redisService.setValuesWithTimeout(resolveAccessToken, "logout", expiration);
  }
}
