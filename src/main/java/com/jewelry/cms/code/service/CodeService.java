package com.jewelry.cms.code.service;

import com.jewelry.cms.code.domain.CodeTO;
import com.jewelry.cms.code.domain.CodeVO;

import java.util.List;
import java.util.Map;

public interface CodeService {
	
	Map<String, Object> findAllCode(CodeTO to);
	
	CodeVO findCodeByCdId(String cdid);
	
	String insertCode(CodeTO to);
	
	String updateCode(CodeTO to);
	
	String deleteCode(String cdid);
	
	List<CodeVO> findAllByUpCdId(String upcdid, Integer cddepth);

	List<CodeVO> findAllByUpCdId(String upcdid, Integer cddepth, String useyn);

	List<CodeVO> findAllByUpCdId(String[] upcdid, Integer cddepth);
	
	//********************************하위코드********************************
	Map<String, Object> findAllSubCode(String upcdid, Integer cddepth);

}
