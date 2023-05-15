package com.jewelry.aop;

import com.jewelry.cms.menu.domain.MenuAuthVO;
import com.jewelry.cms.menu.mapper.MenuAuthMapper;
import com.jewelry.common.domain.CommonTO;
import com.jewelry.common.domain.CommonVO;
import com.jewelry.exception.CustomException;
import com.jewelry.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorityAspect {

  private final MenuAuthMapper menuAuthMapper;

//  @Pointcut(
//    "(" +
//      "execution(* com.jewelry..*ServiceImpl.*(..)) || " +
//      "execution(* com.jewelry.cms..*ServiceImpl.*(..))" +
//    ") && " +
//    "!execution(* com.jewelry.cms..CodeServiceImpl.*(..))"
//  )
  @Pointcut("@annotation(com.jewelry.annotation.MenuAuthAnt)")
  private void authorityPointCut() {}

  /**
   * MenuAuthAnt 어노테이션이 선언된 Service의 메서드의 경우 권한 확인
   * @param pjp
   * @return
   * @throws Throwable
   */
  @Around("authorityPointCut()")
  public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
    log.info("[****************************** 메뉴 권한 조회 AOP(Before) ***********************************");

    MenuAuthVO authVo = null;
    Object[] paramObj = pjp.getArgs();
    for(Object obj : paramObj){
      //CommonTO 파라미터의 값으로 권한 조회
      if(obj instanceof CommonTO){
        authVo = menuAuthMapper.selectUserAuthMenu((CommonTO)obj);
        break;
      }
    }

    //권한자체가 없을 경우 초기화
    if(authVo == null){
      authVo = new MenuAuthVO("N","N","N","N","N");
    }

    String methodName = pjp.getSignature().getName().toLowerCase();
    boolean isIUD = false, isAuthed = false;
    if(methodName.indexOf("insert") == 0){
      isIUD = true;
      isAuthed = ObjectUtils.nullSafeEquals(authVo.getWriteAuth(), "Y") ? true : false;
    }
    else if(methodName.indexOf("update") == 0){
      isIUD = true;
      if(methodName.indexOf("delete") > -1)
        isAuthed = ObjectUtils.nullSafeEquals(authVo.getRemoveAuth(), "Y") ? true : false;
      else
        isAuthed = ObjectUtils.nullSafeEquals(authVo.getModifyAuth(), "Y") ? true : false;
    }
    else if(methodName.indexOf("delete") == 0){
      isIUD = true;
      isAuthed = ObjectUtils.nullSafeEquals(authVo.getRemoveAuth(), "Y") ? true : false;
    }

    if(isIUD && !isAuthed){
      throw new CustomException(ResponseCode.FORBIDDEN);
    }

    Object result = pjp.proceed();
    if(!isIUD) {
      log.info("[****************************** 메뉴 권한 조회 AOP(After) ***********************************");
      if (result instanceof CommonVO) {
        ((CommonVO) result).setMenuAuthVO(authVo);
      } else if (result instanceof Map<?, ?>) {
        ((Map<String, Object>) result).put("menuAuth", authVo);
      }
    }
    return result;
  }
}
