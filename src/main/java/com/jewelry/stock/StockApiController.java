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

	@GetMapping("/list")
	public Map<String, Object> list(final StockTO to){
		return stockService.findAllStock(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(
			@RequestHeader("Authorization") String accessToken,
			@RequestPart(value = "file", required = false) MultipartFile file,
			final StockTO to){
		to.setStockfile(file);
		to.setInpt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = stockService.insertStock(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@GetMapping("/{stockno}")
	public StockVO stock(@PathVariable final Long stockno){
		return stockService.findStockByNo(stockno);
	}

	@GetMapping("/customer/{stockno}")
	public StockVO stockCustomer(@PathVariable final Long stockno){
		return stockService.findStockCustomerByNo(stockno);
	}

	@PatchMapping("/modify/{stockno}")
	public ResponseEntity<Object> modify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long stockno, final StockTO to,
			@RequestPart(value = "file", required = false) MultipartFile file){
		String username = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setStock_no(stockno);
		to.setStockfile(file);
		to.setInpt_id(username);
		to.setUpdt_id(username);
		String result = stockService.updateStock(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@PatchMapping("/stocks/remove")
	public ResponseEntity<Object> ordersRemove(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = stockService.updateStocksToDelete(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PatchMapping("/stocks/sale")
	public ResponseEntity<Object> ordersSale(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = stockService.updateStocksToSale(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@GetMapping("/accumulation/list")
	public Map<String, Object> listAll(final StockTO to){
		return stockService.findAllAccumulationStock(to);
	}

	@PatchMapping("/reg-date/modify")
	public ResponseEntity<Object> regDateModify(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = stockService.updateStocksRegDate(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PatchMapping("/type/modify")
	public ResponseEntity<Object> typeModify(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = stockService.updateStocksType(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PostMapping("/order/customer/write")
	public ResponseEntity<Object> orderCustomerWrite(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		String username = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setInpt_id(username);
		to.setUpdt_id(username);
		String result = stockService.insertCustomerOrder(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PatchMapping("/vender/modify")
	public ResponseEntity<Object> venderModify(
			@RequestHeader("Authorization") String accessToken,
			final StockTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = stockService.updateStocksVender(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
}
