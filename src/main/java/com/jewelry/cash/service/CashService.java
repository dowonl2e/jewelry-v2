package com.jewelry.cash.service;

import com.jewelry.cash.domain.CashTO;
import com.jewelry.cash.domain.CashVO;

import java.util.Map;

public interface CashService {

	Map<String, Object> findAllCash(CashTO to);
	
	CashVO findCash(CashTO to);
	
	String insertCash(CashTO to);
	
	String updateCash(CashTO to);
	
	String updateCashesToDelete(CashTO to);

}
