package com.jewelry.vender;

import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.response.ResponseCode;
import com.jewelry.vender.domain.VenderPayTO;
import com.jewelry.vender.domain.VenderPayVO;
import com.jewelry.vender.domain.VenderTO;
import com.jewelry.vender.domain.VenderVO;
import com.jewelry.vender.service.VenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/vender")
@RequiredArgsConstructor
public class VenderApiController {
	
	private final VenderService venderService;

	private final JwtTokenProvider jwtTokenProvider;

	private final String menuId = "vender";

	@GetMapping("/list")
	public Map<String, Object> findAll(
			@RequestHeader("Authorization") String accessToken,
			final VenderTO to){
		to.setMenuId(menuId);
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		return venderService.findAllVender(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(
			@RequestHeader("Authorization") String accessToken,
			@RequestBody final VenderTO to) { //이쪽으로온 json 데이터 형식을 to에 넣어줌 requestbody
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setInpt_id(userId);
		String result = venderService.insertVender(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@GetMapping("/{venderno}")
	public VenderVO findVenderByNo(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long venderno) {
		VenderTO to = new VenderTO();
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setVender_no(venderno);
		return venderService.findVenderByNo(to);
	}
	
	@PatchMapping("/{venderno}") // URL에 있는 {venderno} 을 아래의 넣기 위해 @PathVariable 을 쓴다.
	public ResponseEntity<Object> modify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long venderno,
			@RequestBody final VenderTO to) {
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setVender_no(venderno);
		to.setUpdt_id(userId);
		String result = venderService.updateVender(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@PatchMapping("/venders/remove")
	public ResponseEntity<Object> venderRemove(
			@RequestHeader("Authorization") String accessToken,
			final VenderTO to){ //파라미터로
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setUpdt_id(userId);
		String result = venderService.updateVenderToDelete(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR; //에러코드를 반환
		return new ResponseEntity<>(response.getStatus());
	}

	@GetMapping("/pay/list")
	public Map<String, Object> findAllVenderPay(
			@RequestHeader("Authorization") String accessToken,
			final VenderPayTO to){
		to.setMenuId(menuId+"/pay");
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		return venderService.findAllVenderPay(to);
	}

	@PostMapping("/pay/write")
	public ResponseEntity<Object> payWrite(
			@RequestHeader("Authorization") String accessToken,
			@RequestBody final VenderPayTO to) { //이쪽으로온 json 데이터 형식을 to에 넣어줌 requestbody
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId+"/pay");
		to.setUser_id(userId);
		to.setInpt_id(userId);
		String result = venderService.insertVenderPay(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@GetMapping("/pay/{payNo}")
	public VenderPayVO findVenderPayByNo(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long payNo) {
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		VenderPayTO to = new VenderPayTO();
		to.setMenuId(menuId+"/pay");
		to.setUser_id(userId);
		to.setPay_no(payNo);
		return venderService.findVenderPayByNo(to);
	}

	@PatchMapping("/pay/modify/{payNo}")
	public ResponseEntity<Object> payModify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long payNo,
			@RequestBody final VenderPayTO to) { //이쪽으로온 json 데이터 형식을 to에 넣어줌 requestbody
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId+"/pay");
		to.setUser_id(userId);
		to.setPay_no(payNo);
		to.setUpdt_id(userId);
		String result = venderService.updateVenderPay(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@PatchMapping("/pays/remove")
	public ResponseEntity<Object> paysRemove(
			@RequestHeader("Authorization") String accessToken,
			final VenderPayTO to) { //이쪽으로온 json 데이터 형식을 to에 넣어줌 requestbody
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId+"/pay");
		to.setUser_id(userId);
		to.setUpdt_id(userId);
		String result = venderService.updateVenderPaysToDelete(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
}
