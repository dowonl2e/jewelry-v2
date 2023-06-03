package com.jewelry.sale.service;

import com.jewelry.sale.domain.SaleTO;

import java.util.Map;

public interface SaleService {

	Map<String, Object> findAllSale(SaleTO to);
	
	String deleteSalesToStock(SaleTO to);
	
	String updateSalesCustomer(SaleTO to);
	
	String updateSalesDate(SaleTO to);
	
	
}
