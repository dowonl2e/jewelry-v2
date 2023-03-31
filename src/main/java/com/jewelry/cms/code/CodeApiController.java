package com.jewelry.cms.code;

import com.jewelry.cms.code.domain.CodeTO;
import com.jewelry.cms.code.domain.CodeVO;
import com.jewelry.cms.code.service.CodeService;
import com.jewelry.response.ResponseCode;
import com.jewelry.user.entity.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/code")
public class CodeApiController {

	@Autowired
	private CodeService codeService;
	
	@Autowired
    private HttpSession session;
	
	@GetMapping("/list")
	public Map<String, Object> findAll(final CodeTO to){
		to.setCd_depth(1);
		return codeService.findAllCode(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(@RequestBody final CodeTO to) {
		to.setInpt_id(((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername());
		String result = codeService.insertCode(to);
		
		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return  new ResponseEntity<>(response.getStatus());

	}

	@GetMapping("/{cdid}")
	public CodeVO findCodeByCdId(@PathVariable final String cdid) {
		return codeService.findCodeByCdId(cdid);
	}
	
	@PatchMapping("/{cdid}")
	public ResponseEntity<Object> modify(@PathVariable final String cdid, @RequestBody final CodeTO to){
		to.setUpdt_id(((CustomUserDetails)session.getAttribute("USER_INFO")).getUsername());
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
