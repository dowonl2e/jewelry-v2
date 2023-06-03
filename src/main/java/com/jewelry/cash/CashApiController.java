package com.jewelry.cash;

import com.jewelry.cash.domain.CashTO;
import com.jewelry.cash.domain.CashVO;
import com.jewelry.cash.service.CashService;
import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cash")
@RequiredArgsConstructor
public class CashApiController {

	private final CashService cashService;

	private final JwtTokenProvider jwtTokenProvider;

	private final String menuId = "cash";

	@GetMapping("/list")
	public Map<String, Object> list(
			@RequestHeader("Authorization") String accessToken,
			final CashTO to){
		to.setMenuId(menuId);
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		return cashService.findAllCash(to);
	}

	@PostMapping("/write")
	public ResponseEntity<Object> write(
			@RequestHeader("Authorization") String accessToken,
			final CashTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setInpt_id(userId);
		String result = cashService.insertCash(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@GetMapping("/{cashno}")
	public CashVO view(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long cashno){
		CashTO to = new CashTO();
		to.setMenuId(menuId);
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		to.setCash_no(cashno);
		return cashService.findCash(to);
	}
	

	@PatchMapping("/modify/{cashno}")
	public ResponseEntity<Object> modify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long cashno,
			final CashTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setUpdt_id(userId);
		to.setCash_no(cashno);
		to.setInpt_id(userId);
		String result = cashService.updateCash(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PatchMapping("/cashes/remove")
	public ResponseEntity<Object> cashesRemove(
			@RequestHeader("Authorization") String accessToken,
			final CashTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setUpdt_id(userId);
		String result = cashService.updateCashesToDelete(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
}
