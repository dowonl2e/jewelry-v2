package com.jewelry.cms.code.service.impl;

import com.jewelry.annotation.MenuAuthAnt;
import com.jewelry.cms.code.domain.CodeTO;
import com.jewelry.cms.code.domain.CodeVO;
import com.jewelry.cms.code.mapper.CodeMapper;
import com.jewelry.cms.code.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {
	
	private final CodeMapper codeMapper;

	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public Map<String, Object> findAllCode(CodeTO to) {
		Map<String, Object> response = new HashMap<>();

		to.setTotalcount(codeMapper.selectCodeListCount(to));
		response.put("list", codeMapper.selectCodeList(to));
		response.put("params", to);
		
		return response;
	}

	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public CodeVO findCodeByCdId(CodeTO to) {
		return codeMapper.selectCode(to.getCd_id());
	}

	@Transactional
	@MenuAuthAnt
	@Override
	public String insertCode(CodeTO to) {
		int res = 0;
		try {
			res = codeMapper.insertCode(to);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return res > 0 ? "success" : "fail";
	}

	@Transactional
	@MenuAuthAnt
	@Override
	public String updateCode(CodeTO to) {
		int res = 0;
		try {
			res = codeMapper.updateCode(to);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return res > 0 ? "success" : "fail";
	}

	@Transactional
	@MenuAuthAnt
	@Override
	public String deleteCode(CodeTO to) {
		int res = 0;
		try {
			codeMapper.deleteLowCodeByCdId(to.getCd_id());
			res = codeMapper.deleteCode(to.getCd_id());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return res > 0 ? "success" : "fail";
	}

	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public List<CodeVO> findAllByUpCdId(String upcdid, Integer cddepth) {
		return findAllByUpCdId(upcdid, cddepth, "Y");
	}

	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public List<CodeVO> findAllByUpCdId(String upcdid, Integer cddepth, String useyn) {
		CodeTO to = new CodeTO();
		to.setUp_cd_id(upcdid);
		to.setCd_depth(cddepth);
		to.setUse_yn(useyn);
		return codeMapper.selectCodeListByUpCdId(to);
	}

	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public List<CodeVO> findAllByUpCdId(String[] upcdid, Integer cddepth) {
		CodeTO to = new CodeTO();
		to.setUp_cd_id_arr(upcdid);
		to.setCd_depth(cddepth);
		return codeMapper.selectCodeListByUpCdIdArr(to);
	}

	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public Map<String, Object> findAllSubCode(CodeTO to) {
		Map<String, Object> response = new HashMap<>();

		response.put("vo", codeMapper.selectCode(to.getUp_cd_id()));
		response.put("list", codeMapper.selectSubCodeList(to));
		
		return response;
	}
	
	
}
