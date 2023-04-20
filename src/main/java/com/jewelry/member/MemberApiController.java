package com.jewelry.member;

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

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberApiController {

  private final UserService userService;

  private final JwtTokenProvider jwtTokenProvider;

  @GetMapping("/profile")
  public UserVO findMemberProfile(@RequestHeader("Authorization") String accessToken) {
    return userService.findUser(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
  }

  @PostMapping("/profile/info")
  public UserVO findMemberByToken(@RequestHeader("Authorization") String accessToken) {
    return userService.findUserByToken(accessToken);
  }

  @PatchMapping("/profile/modify")
  public ResponseEntity<Object> modify(
      @RequestHeader("Authorization") String accessToken,
      @RequestBody final UserTO to) {
    String userid = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
    to.setUser_id(userid);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    to.setUser_pwd(ObjectUtils.isEmpty(to.getUser_pwd()) ? null : passwordEncoder.encode(to.getUser_pwd()));
    to.setUpdt_id(userid);
    String result = userService.updateUser(to);

    ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
    return new ResponseEntity<>(response.getStatus());
  }

}
