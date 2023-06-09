package com.jewelry.sale;

import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.response.ResponseCode;
import com.jewelry.sale.domain.SaleTO;
import com.jewelry.sale.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sale")
@RequiredArgsConstructor
public class SaleApiController {

	private final SaleService saleService;

	private final JwtTokenProvider jwtTokenProvider;

	private final String menuId = "sale";

	@GetMapping("/list")
	public Map<String, Object> list(
			@RequestHeader("Authorization") String accessToken,
			final SaleTO to){
		to.setMenuId(menuId);
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		return saleService.findAllSale(to);
	}
	
	@PatchMapping("/sales/stock/modify")
	public ResponseEntity<Object> salesStockModify(
			@RequestHeader("Authorization") String accessToken,
			final SaleTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setUpdt_id(userId);
		String result = saleService.deleteSalesToStock(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@PatchMapping("/sales/customer/modify")
	public ResponseEntity<Object> salesCustomerModify(
			@RequestHeader("Authorization") String accessToken,
			final SaleTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setUpdt_id(userId);
		String result = saleService.updateSalesCustomer(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@PatchMapping("/sales/date/modify")
	public ResponseEntity<Object> salesDateModify(
			@RequestHeader("Authorization") String accessToken,
			final SaleTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setUpdt_id(userId);
		String result = saleService.updateSalesDate(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
}
