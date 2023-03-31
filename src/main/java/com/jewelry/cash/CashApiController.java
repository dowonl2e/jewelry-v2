package com.jewelry.cash;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelry.cash.domain.CashTO;
import com.jewelry.cash.domain.CashVO;
import com.jewelry.cash.service.CashService;
import com.jewelry.response.ResponseCode;
import com.jewelry.response.ResponseCode;
import com.jewelry.user.entity.CustomUserDetails;

@RestController
@RequestMapping("/api/cash")
public class CashApiController {

	@Autowired
	private CashService cashService;
	
	@Autowired
	private HttpSession session;
	
	@GetMapping("/list")
	public Map<String, Object> list(final CashTO to){
		return cashService.findAllCash(to);
	}

	@PostMapping("/write")
	public ResponseEntity<Object> write(final CashTO to){
		to.setInpt_id(((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername());
		String result = cashService.insertCash(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@GetMapping("/{cashno}")
	public CashVO view(@PathVariable final Long cashno){
		return cashService.findCash(cashno);
	}
	

	@PatchMapping("/modify/{cashno}")
	public ResponseEntity<Object> modify(@PathVariable final Long cashno, final CashTO to){
		String username = ((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername();
		to.setUpdt_id(username);
		to.setInpt_id(username);
		to.setCash_no(cashno);
		String result = cashService.updateCash(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PatchMapping("/cashes/remove")
	public ResponseEntity<Object> cashesRemove(final CashTO to){
		to.setUpdt_id(((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername());
		String result = cashService.updateCashesToDelete(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
}
