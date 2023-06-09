package com.jewelry.vender.service.impl;

import com.jewelry.annotation.MenuAuthAnt;
import com.jewelry.vender.domain.VenderPayTO;
import com.jewelry.vender.domain.VenderPayVO;
import com.jewelry.vender.domain.VenderTO;
import com.jewelry.vender.domain.VenderVO;
import com.jewelry.vender.mapper.VenderMapper;
import com.jewelry.vender.service.VenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VenderServiceImpl implements VenderService {

	private final VenderMapper venderMapper;
	
	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public Map<String, Object> findAllVender(VenderTO to) {
		Map<String, Object> response = new HashMap<>();
		
		to.setTotalcount(venderMapper.selectVenderListCount(to));
		response.put("list", venderMapper.selectVenderList(to)); 
		response.put("params", to);
				
		return response;
	}

	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public VenderVO findVenderByNo(VenderTO to) {
		return venderMapper.selectVender(to.getVender_no());
	}
	
	@Transactional
	@MenuAuthAnt
	@Override
	public String insertVender(VenderTO to) {
		String result = "fail";
		try {
			result = venderMapper.insertVender(to) > 0 ? "success" : "fail";
		}
		catch(Exception e) {
			e.printStackTrace();
			result = "fail";
		}
		return result;
	}

	@Transactional
	@MenuAuthAnt
	@Override
	public String updateVender(VenderTO to) {
		String result = "fail";
		try {
			result = venderMapper.updateVender(to) > 0 ? "success" : "fail";
		}
		catch(Exception e) {
			e.printStackTrace();
			result = "fail";
		}
		return result;
	}
	
	@Transactional
	@MenuAuthAnt
	@Override
	public String updateVenderToDelete(VenderTO to) {
		String result = "fail";
		try {
			Long[] vender_no_arr = to.getVender_no_arr();
			if(vender_no_arr != null && vender_no_arr.length > 0) {
			//	int res = venderMapper.updateVenderToDelete(to);  
			//	result = res > 0? "success" : "fail";
				result = venderMapper.updateVenderToDelete(to) > 0 ? "success" : "fail"; // 한줄로 표현
			
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result = "fail";
		}
		return result;
	}

	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public Map<String, Object> findAllVenderPay(VenderPayTO to) {
		Map<String, Object> response = new HashMap<>();
		
		to.setTotalcount(venderMapper.selectVenderPayListCount(to));
		response.put("list", venderMapper.selectVenderPayList(to)); 
		response.put("params", to);
		
		return response;
	}

	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public VenderPayVO findVenderPayByNo(VenderPayTO to) {
		return venderMapper.selectVenderPay(to.getPay_no());
	}

	@Transactional
	@MenuAuthAnt
	@Override
	public String insertVenderPay(VenderPayTO to) {
		String result = "fail";
		try {
			int res = venderMapper.insertVenderPay(to);
			result = res > 0 ? "success" : "fail";
		}
		catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result = "fail";
		}
		return result;
	}

	@Transactional
	@MenuAuthAnt
	@Override
	public String updateVenderPay(VenderPayTO to) {
		String result = "fail";
		try {
			int res = venderMapper.updateVenderPay(to);
			result = res > 0 ? "success" : "fail";
		}
		catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result = "fail";
		}
		return result;
	}
	
	@Transactional
	@MenuAuthAnt
	@Override
	public String updateVenderPaysToDelete(VenderPayTO to) {
		String result = "fail";
		try {
			if(to.getPay_no_arr() != null && to.getPay_no_arr().length > 0) {
				int res = venderMapper.updateVenderPaysToDelete(to);
				result = res > 0 ? "success" : "fail";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result = "fail";
		}
		return result;
	}
}
