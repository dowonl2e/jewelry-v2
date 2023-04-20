package com.jewelry.catalog;

import com.jewelry.catalog.domain.CatalogTO;
import com.jewelry.catalog.domain.CatalogVO;
import com.jewelry.catalog.service.CatalogService;
import com.jewelry.config.provider.JwtTokenProvider;
import com.jewelry.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogApiController {

	private final CatalogService catalogService;

	private final JwtTokenProvider jwtTokenProvider;

	@GetMapping("/list")
	public Map<String, Object> list(final CatalogTO to){
		return catalogService.findAllCatalog(to);
	}
	
	@PostMapping("/write")
	public ResponseEntity<Object> write(
			@RequestHeader("Authorization") String accessToken,
			@RequestPart(value = "file", required = false) MultipartFile file,
			final CatalogTO to){
		to.setCatalogfile(file);
		to.setInpt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = catalogService.insertCatalog(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@GetMapping("/{catalogno}")
	public CatalogVO catalog(@PathVariable final Long catalogno){
		return catalogService.findCatalogByNo(catalogno);
	}
	
	@PatchMapping("/modify/{catalogno}")
	public ResponseEntity<Object> modify(
			@RequestHeader("Authorization") String accessToken,
			@PathVariable final Long catalogno, CatalogTO to,
			@RequestPart(value = "file", required = false) MultipartFile file){
		String userid = jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken));
		to.setCatalogfile(file);
		to.setCatalog_no(catalogno);
		to.setInpt_id(userid);
		to.setUpdt_id(userid);
		String result = catalogService.updateCatalog(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}

	@PatchMapping("/catalogs/remove")
	public ResponseEntity<Object> repairsRemove(
			@RequestHeader("Authorization") String accessToken,
			final CatalogTO to){
		to.setUpdt_id(jwtTokenProvider.getPrincipal(jwtTokenProvider.resolveToken(accessToken)));
		String result = catalogService.updateCatalogsToDelete(to);

		ResponseCode response = result.equals("success") ? ResponseCode.SUCCESS : ResponseCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(response.getStatus());
	}
}
