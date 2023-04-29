package com.jewelry.repair;

import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.repair.domain.RepairTO;
import com.jewelry.repair.domain.RepairVO;
import com.jewelry.repair.service.RepairService;
import com.jewelry.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/repair")
@RequiredArgsConstructor
public class RepairApiController {
	
	private final RepairService repairService;

	private final JwtTokenProvider jwtTokenProvider;

	@GetMapping("/list")
	public Map<String, Object> list(final RepairTO to){
		return repairService.findAllRepair(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(
			@RequestHeader("Authorization") String accessToken,
			@RequestPart(value = "file", required = false) MultipartFile file,
			final RepairTO to){
		to.setRepairfile(file);
		to.setInpt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = repairService.insertRepair(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@GetMapping("/{repairno}")
	public RepairVO repair(@PathVariable final Long repairno){
		return repairService.findRepair(repairno);
	}
	
	@PatchMapping("/modify/{repairno}")
	public ResponseEntity<Object> modify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long repairno,
			@RequestPart(value = "file", required = false) MultipartFile file,
			RepairTO to){
		String userid = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setRepairfile(file);
		to.setRepair_no(repairno);
		to.setInpt_id(userid);
		to.setUpdt_id(userid);
		String result = repairService.updateRepair(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PatchMapping("/repairs/remove")
	public ResponseEntity<Object> repairsRemove(
			@RequestHeader("Authorization") String accessToken,
			final RepairTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = repairService.updateRepairsToDelete(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@PatchMapping("/repairs/complete")
	public ResponseEntity<Object> repairscomplete(
			@RequestHeader("Authorization") String accessToken,
			final RepairTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = repairService.updateRepairsToComplete(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
	
	@GetMapping("/customer/list/{customerno}")
	public Map<String, Object> customerList(@PathVariable final Long customerno, final RepairTO to){
		to.setCustomer_no(customerno);
		return repairService.findAllCustomerRepair(to);
	}
}
