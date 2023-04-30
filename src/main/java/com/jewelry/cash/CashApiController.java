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

	@GetMapping("/list")
	public Map<String, Object> list(final CashTO to){
		return cashService.findAllCash(to);
	}

	@PostMapping("/write")
	public ResponseEntity<Object> write(
			@RequestHeader("Authorization") String accessToken,
			final CashTO to){
		to.setInpt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = cashService.insertCash(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@GetMapping("/{cashno}")
	public CashVO view(@PathVariable final Long cashno){
		return cashService.findCash(cashno);
	}
	

	@PatchMapping("/modify/{cashno}")
	public ResponseEntity<Object> modify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long cashno,
			final CashTO to){
		String userid = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setUpdt_id(userid);
		to.setInpt_id(userid);
		to.setCash_no(cashno);
		String result = cashService.updateCash(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PatchMapping("/cashes/remove")
	public ResponseEntity<Object> cashesRemove(
			@RequestHeader("Authorization") String accessToken,
			final CashTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = cashService.updateCashesToDelete(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
}
