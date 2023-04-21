package com.jewelry.order;

import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.order.domain.OrderTO;
import com.jewelry.order.domain.OrderVO;
import com.jewelry.order.service.OrderService;
import com.jewelry.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderApiController {
	
	private final OrderService orderService;

	private final JwtTokenProvider jwtTokenProvider;

	@GetMapping("/list")
	public Map<String, Object> list(final OrderTO to){
		return orderService.findAllOrder(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(
			@RequestHeader("Authorization") String accessToken,
			@RequestPart(value = "file", required = false) MultipartFile file,
			final OrderTO to){
		to.setOrderfile(file);
		to.setInpt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = orderService.insertOrder(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@GetMapping("/{orderno}")
	public OrderVO order(@PathVariable final Long orderno){
		return orderService.findOrderByNo(orderno);
	}
	
	@PatchMapping("/modify/{orderno}")
	public ResponseEntity<Object> modify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long orderno, final OrderTO to,
			@RequestPart(value = "file", required = false) MultipartFile file){
		String userid = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setOrder_no(orderno);
		to.setOrderfile(file);
		to.setInpt_id(userid);
		to.setUpdt_id(userid);
		String result = orderService.updateOrder(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@PatchMapping("/step/modify")
	public ResponseEntity<Object> stepModify(
			@RequestHeader("Authorization") String accessToken,
			final OrderTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = orderService.updateOrdersStep(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PatchMapping("/orders/remove")
	public ResponseEntity<Object> ordersRemove(
			@RequestHeader("Authorization") String accessToken,
			final OrderTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = orderService.updateOrdersToDelete(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@GetMapping("/schedule/list")
	public Map<String, Object> scheduleList(final OrderTO to){
		to.setOrder_step("A");
		return orderService.findAllOrder(to);
	}

	@GetMapping("/stocked/list")
	public Map<String, Object> stockedList(final OrderTO to){
		to.setOrder_step("B");
		return orderService.findAllOrder(to);
	}

	@PatchMapping("/customer/modify")
	public ResponseEntity<Object> customerModify(
			@RequestHeader("Authorization") String accessToken,
			final OrderTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = orderService.updateOrdersCustomer(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}	

	@PatchMapping("/vender/modify")
	public ResponseEntity<Object> venderModify(
			@RequestHeader("Authorization") String accessToken,
			final OrderTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = orderService.updateOrdersVender(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PostMapping("/list/nos")
	public Map<String, Object> listByOrdersNo(final OrderTO to){
		return orderService.findAllOrdersNo(to);
	}
	
	@GetMapping("/customer/list/{customerno}")
	public Map<String, Object> orderCustomerList(@PathVariable("customerno") final Long customerno, final OrderTO to){
		to.setCustomer_no(customerno);
		return orderService.findAllCustomerOrder(to);
	}

	@GetMapping("/customer/list/{customerno}/{orderstep}")
	public Map<String, Object> orderCustomerList(@PathVariable("customerno") final Long customerno, @PathVariable("orderstep") final String orderstep, final OrderTO to){
		to.setOrder_step(orderstep);
		to.setCustomer_no(customerno);
		return orderService.findAllCustomerOrder(to);
	}

}
