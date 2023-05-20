package com.jewelry.customer;

import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.customer.domain.CustomerTO;
import com.jewelry.customer.domain.CustomerVO;
import com.jewelry.customer.service.CustomerService;
import com.jewelry.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerApiController {
	
	private final CustomerService customerService;

	private final JwtTokenProvider jwtTokenProvider;

	private final String menuId = "customer";

	@GetMapping("/list")
	public Map<String, Object> findAll(
			@RequestHeader("Authorization") String accessToken,
			final CustomerTO to){
		to.setMenuId(menuId);
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		return customerService.findAllCustomer(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(
			@RequestHeader("Authorization") String accessToken,
			@RequestBody final CustomerTO to) {
		to.setInpt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = customerService.insertCustomer(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return  new ResponseEntity<>(response.getStatus());
	}

	@GetMapping("/{customerno}")
	public CustomerVO findCodeByCdId(@PathVariable final Long customerno) {
		return customerService.findCustomerByNo(customerno);
	}
	
	@PatchMapping("/{customerno}")
	public ResponseEntity<Object> modify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long customerno,
			@RequestBody final CustomerTO to) {
		to.setCustomer_no(customerno);
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = customerService.updateCustomer(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return  new ResponseEntity<>(response.getStatus());
	}
	
	@DeleteMapping("/{customerno}")
	public ResponseEntity<Object> remove(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long customerno) {
		CustomerTO to = new CustomerTO();
		to.setCustomer_no(customerno);
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = customerService.updateCustomerToDelete(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return  new ResponseEntity<>(response.getStatus());
	}

	@PatchMapping("/customers/remove")
	public ResponseEntity<Object> customersRemove(
			@RequestHeader("Authorization") String accessToken,
			final CustomerTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = customerService.updateCustomersToDelete(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
}
