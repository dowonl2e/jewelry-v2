package com.jewelry.customer;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelry.customer.domain.CustomerTO;
import com.jewelry.customer.domain.CustomerVO;
import com.jewelry.customer.service.CustomerService;
import com.jewelry.response.ResponseCode;
import com.jewelry.user.entity.CustomUserDetails;

@RestController
@RequestMapping("/api/customer")
public class CustomerApiController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private HttpSession session;
	
	@GetMapping("/list")
	public Map<String, Object> findAll(final CustomerTO to){
		return customerService.findAllCustomer(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(@RequestBody final CustomerTO to) {
		to.setInpt_id(((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername());
		String result = customerService.insertCustomer(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return  new ResponseEntity<>(response.getStatus());
	}

	@GetMapping("/{customerno}")
	public CustomerVO findCodeByCdId(@PathVariable final Long customerno) {
		return customerService.findCustomerByNo(customerno);
	}
	
	@PatchMapping("/{customerno}")
	public ResponseEntity<Object> modify(@PathVariable final Long customerno, @RequestBody final CustomerTO to) {
		to.setCustomer_no(customerno);
		to.setUpdt_id(((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername());
		String result = customerService.updateCustomer(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return  new ResponseEntity<>(response.getStatus());
	}
	
	@DeleteMapping("/{customerno}")
	public ResponseEntity<Object> remove(@PathVariable final Long customerno) {
		CustomerTO to = new CustomerTO();
		to.setCustomer_no(customerno);
		to.setUpdt_id(((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername());
		String result = customerService.updateCustomerToDelete(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return  new ResponseEntity<>(response.getStatus());
	}

	
}
