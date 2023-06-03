package com.jewelry.vender.service;

import com.jewelry.vender.domain.VenderPayTO;
import com.jewelry.vender.domain.VenderPayVO;
import com.jewelry.vender.domain.VenderTO;
import com.jewelry.vender.domain.VenderVO;

import java.util.Map;

public interface VenderService {

	Map<String, Object> findAllVender(VenderTO to);
	
	VenderVO findVenderByNo(VenderTO to);
	
	String insertVender(VenderTO to);
	
	String updateVender(VenderTO to);
	
	String updateVenderToDelete(VenderTO to);
	
	Map<String, Object> findAllVenderPay(VenderPayTO to);

	VenderPayVO findVenderPayByNo(VenderPayTO to);
	
	String insertVenderPay(VenderPayTO to);
	
	String updateVenderPay(VenderPayTO to);
	
	String updateVenderPaysToDelete(VenderPayTO to);
	

}
