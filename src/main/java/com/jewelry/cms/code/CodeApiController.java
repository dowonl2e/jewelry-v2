package com.jewelry.cms.code;

import com.jewelry.cms.code.domain.CodeTO;
import com.jewelry.cms.code.domain.CodeVO;
import com.jewelry.cms.code.service.CodeService;
import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/code")
@RequiredArgsConstructor
public class CodeApiController {

	private final CodeService codeService;

	private final JwtTokenProvider jwtTokenProvider;

	@GetMapping("/list")
	public Map<String, Object> findAll(final CodeTO to){
		to.setCd_depth(1);
		return codeService.findAllCode(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(
			@RequestHeader("Authorization") String accessToken,
			@RequestBody final CodeTO to) {
		to.setInpt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = codeService.insertCode(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return  new ResponseEntity<>(response.getStatus());

	}

	@GetMapping("/{cdid}")
	public CodeVO findCodeByCdId(@PathVariable final String cdid) {
		return codeService.findCodeByCdId(cdid);
	}
	
	@PatchMapping("/{cdid}")
	public ResponseEntity<Object> modify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final String cdid,
			@RequestBody final CodeTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		to.setCd_id(cdid);
		String result = codeService.updateCode(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@DeleteMapping("/{cdid}")
	public String remove(@PathVariable final String cdid) {
		return codeService.deleteCode(cdid);
	}
	
	//********************************하위코드********************************
	@GetMapping("/list/{upcdid}/{cddepth}")
	public Map<String, Object> findAll(@PathVariable("upcdid") final String cdid, @PathVariable("cddepth") final Integer cddepth){
		return codeService.findAllSubCode(cdid, cddepth);
	}
}
