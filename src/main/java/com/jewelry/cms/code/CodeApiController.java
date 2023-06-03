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

	private final String menuId = "code";

	@GetMapping("/list")
	public Map<String, Object> findAll(
			@RequestHeader("Authorization") String accessToken,
			final CodeTO to){
		to.setMenuId(menuId);
		to.setUser_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		to.setCd_depth(1);
		return codeService.findAllCode(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(
			@RequestHeader("Authorization") String accessToken,
			@RequestBody final CodeTO to) {
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setInpt_id(userId);
		String result = codeService.insertCode(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return  new ResponseEntity<>(response.getStatus());

	}

	@GetMapping("/{cdid}")
	public CodeVO findCodeByCdId(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final String cdid) {
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		CodeTO to = new CodeTO();
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setCd_id(cdid);
		return codeService.findCodeByCdId(to);
	}
	
	@PatchMapping("/{cdid}")
	public ResponseEntity<Object> modify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final String cdid,
			@RequestBody final CodeTO to){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setUpdt_id(userId);
		to.setCd_id(cdid);
		String result = codeService.updateCode(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@DeleteMapping("/{cdid}")
	public String remove(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final String cdid) {
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		CodeTO to = new CodeTO();
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setCd_id(cdid);
		return codeService.deleteCode(to);
	}
	
	//********************************하위코드********************************
	@GetMapping("/list/{upcdid}/{cddepth}")
	public Map<String, Object> findAll(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable("upcdid") final String upcdid,
			@PathVariable("cddepth") final Integer cddepth){
		String userId = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		CodeTO to = new CodeTO();
		to.setMenuId(menuId);
		to.setUser_id(userId);
		to.setUp_cd_id(upcdid);
		to.setCd_depth(cddepth);
		return codeService.findAllSubCode(to);
	}
}
