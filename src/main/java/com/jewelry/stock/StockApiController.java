package com.jewelry.stock;

import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.response.ResponseCode;
import com.jewelry.stock.domain.StockTO;
import com.jewelry.stock.domain.StockVO;
import com.jewelry.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockApiController {
	
	private final StockService stockService;

	private final JwtTokenProvider jwtTokenProvider;

	private final String menuId = "stock";
	@GetMapping("/list")
	public Map<String, Object> list(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		to.setMenuId(menuId);
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		return stockService.findAllStock(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(
			@RequestHeader("Authorization") String accessToken,
			@RequestPart(value = "file", required = false) MultipartFile file,
			final StockTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setStockfile(file);
		to.setInpt_id(userId);
		String result = stockService.insertStock(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@GetMapping("/{stockno}")
	public StockVO stock(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long stockno){
		StockTO to = new StockTO();
		to.setMenuId(menuId);
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		to.setStock_no(stockno);
		return stockService.findStockByNo(to);
	}

	@GetMapping("/customer/{stockno}")
	public StockVO stockCustomer(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long stockno){
		StockTO to = new StockTO();
		to.setMenuId(menuId);
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		to.setStock_no(stockno);
		return stockService.findStockCustomerByNo(to);
	}

	@PatchMapping("/modify/{stockno}")
	public ResponseEntity<Object> modify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long stockno, final StockTO to,
			@RequestPart(value = "file", required = false) MultipartFile file){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setStock_no(stockno);
		to.setStockfile(file);
		to.setInpt_id(userId);
		to.setUpdt_id(userId);
		String result = stockService.updateStock(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@PatchMapping("/stocks/remove")
	public ResponseEntity<Object> ordersRemove(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(to.getMenuId() == null ? menuId : to.getMenuId());
		to.setUser_id(userId);
		to.setUpdt_id(userId);
		String result = stockService.updateStocksToDelete(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PatchMapping("/stocks/sale")
	public ResponseEntity<Object> ordersSale(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setUpdt_id(userId);
		String result = stockService.updateStocksToSale(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@GetMapping("/accumulation/list")
	public Map<String, Object> listAll(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		to.setMenuId(menuId+"/accumulation");
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		return stockService.findAllAccumulationStock(to);
	}

	@PatchMapping("/reg-date/modify")
	public ResponseEntity<Object> regDateModify(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setUpdt_id(userId);
		String result = stockService.updateStocksRegDate(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PatchMapping("/type/modify")
	public ResponseEntity<Object> typeModify(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setUpdt_id(userId);
		String result = stockService.updateStocksType(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PostMapping("/order/customer/write")
	public ResponseEntity<Object> orderCustomerWrite(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setInpt_id(userId);
		to.setUpdt_id(userId);
		String result = stockService.insertCustomerOrder(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PatchMapping("/vender/modify")
	public ResponseEntity<Object> venderModify(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setUpdt_id(userId);
		String result = stockService.updateStocksVender(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
}
