package com.jewelry.sale;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelry.response.ResponseCode;
import com.jewelry.sale.domain.SaleTO;
import com.jewelry.sale.service.SaleService;
import com.jewelry.user.entity.CustomUserDetails;

@RestController
@RequestMapping("/api/sale")
public class SaleApiController {

	@Autowired
	private SaleService saleService;
	
	
	@Autowired
	private HttpSession session;
	
	
	@GetMapping("/list")
	public Map<String, Object> list(final SaleTO to){
		return saleService.findAllSale(to);
	}
	
	@PatchMapping("/sales/stock/modify")
	public ResponseEntity<Object> salesStockModify(final SaleTO to){
		to.setUpdt_id(((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername());
		String result = saleService.updateSalesToStock(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@PatchMapping("/sales/customer/modify")
	public ResponseEntity<Object> salesCustomerModify(final SaleTO to){
		to.setUpdt_id(((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername());
		String result = saleService.updateSalesCustomer(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@PatchMapping("/sales/date/modify")
	public ResponseEntity<Object> salesDateModify(final SaleTO to){
		to.setUpdt_id(((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername());
		String result = saleService.updateSalesDate(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
}
